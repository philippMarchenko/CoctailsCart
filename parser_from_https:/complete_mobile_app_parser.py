#!/usr/bin/env python3
"""
Complete IBA Cocktails Parser - Extract ALL Cocktails with Video Links
======================================================================

This script extracts ALL cocktails from the IBA World Cocktails website including:
✅ Basic cocktail information (title, image, category, views)
✅ Detailed recipe information (ingredients, method, garnish, glass)
✅ YouTube video tutorial links for each cocktail
✅ Mobile app optimized JSON structure with enums
✅ Search and filter optimization
✅ Complexity and alcohol strength ratings

Usage:
    python3 complete_mobile_app_parser.py           # Parse ALL cocktails (default)
    python3 complete_mobile_app_parser.py --test    # Parse first 10 for testing

Output:
    - iba_cocktails_all.json           # Basic cocktail data
    - iba_cocktails_all_detailed.json  # With detailed info and videos
    - iba_cocktails_complete.json      # Final mobile app optimized JSON

Features:
    • Video extraction: YouTube tutorial links for each cocktail
    • Method/garnish separation: Clean separation of preparation steps
    • Enum structures: Mobile app friendly dropdowns and filters
    • Ingredient categorization: Spirits, liqueurs, mixers, etc.
    • Search optimization: Full-text search fields
    • No empty fields: Removed unused description/history/variations
"""

import json
import time
import re
import requests
from bs4 import BeautifulSoup
from urllib.parse import urljoin, urlparse


class CompleteIBACocktailParser:
    """Complete parser that handles everything from basic extraction to enhanced mobile app JSON"""
    
    def __init__(self, base_url: str = "https://iba-world.com/cocktails/all-cocktails/page/"):
        self.base_url = base_url
        self.session = requests.Session()
        self.session.headers.update({
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36'
        })
    
    def extract_cocktails_from_page(self, page_num: int) -> list:
        """Extract cocktails from a single page"""
        page_url = f"{self.base_url}{page_num}/"
        
        try:
            response = self.session.get(page_url, timeout=10)
            response.raise_for_status()
            
            soup = BeautifulSoup(response.content, 'html.parser')
            
            cocktails = []
            cocktail_elements = soup.find_all('div', class_='cocktail')
            
            for element in cocktail_elements:
                try:
                    # Extract title from h2 element
                    h2_element = element.find('h2')
                    if not h2_element:
                        continue
                    
                    title = h2_element.get_text(strip=True)
                    
                    # Extract URL from the main link (usually in the a tag around the image or h2)
                    link_element = element.find('a')
                    if not link_element:
                        continue
                    
                    cocktail_url = urljoin(self.base_url, link_element['href'])
                    
                    # Extract image from picture/img element
                    img_element = element.find('img')
                    image_url = ""
                    if img_element and img_element.get('src'):
                        image_url = urljoin(self.base_url, img_element['src'])
                    
                    # Try to extract category and views - these might be in specific spans or divs
                    category = ""
                    views = ""
                    
                    # Look for category information in cocktail-category div
                    category_div = element.find('div', class_='cocktail-category')
                    if category_div:
                        category = category_div.get_text(strip=True)
                    
                    # Look for views information in cocktail-views div
                    views_div = element.find('div', class_='cocktail-views')
                    if views_div:
                        views = views_div.get_text(strip=True)
                    
                    cocktails.append({
                        "title": title,
                        "image_url": image_url,
                        "cocktail_url": cocktail_url,
                        "category": category,
                        "views": views
                    })
                    
                except Exception as e:
                    print(f"Error extracting cocktail from element: {e}")
                    continue
            
            return cocktails
            
        except Exception as e:
            print(f"Error fetching page {page_num}: {e}")
            return []
    
    def extract_all_cocktails(self, max_pages: int = 10) -> list:
        """Extract cocktails from all pages"""
        print("🍸 Starting complete IBA cocktails extraction...")
        print("=" * 50)
        
        all_cocktails = []
        page_num = 1
        empty_pages = 0
        
        while page_num <= max_pages:
            print(f"Extracting page {page_num}...")
            
            cocktails = self.extract_cocktails_from_page(page_num)
            
            if not cocktails:
                empty_pages += 1
                if empty_pages >= 2:  # Stop after 2 consecutive empty pages
                    print(f"No more cocktails found. Stopping at page {page_num}")
                    break
            else:
                empty_pages = 0
                all_cocktails.extend(cocktails)
                print(f"Found {len(cocktails)} cocktails on page {page_num}")
            
            page_num += 1
            time.sleep(0.5)  # Rate limiting
        
        print(f"\n📊 Total cocktails extracted: {len(all_cocktails)}")
        return all_cocktails
    
    def separate_method_garnish_improved(self, instructions):
        """Better separation of preparation method from garnish decoration"""
        if not instructions:
            return "", ""
        
        # Clean up the instructions first
        instructions = instructions.strip()
        
        # Split into sentences
        sentences = re.split(r'(?<=[.!?])\s+', instructions)
        
        method_sentences = []
        garnish_sentences = []
        
        garnish_indicators = [
            r'\bgarnish\s+with\b',
            r'\bsprinkle\b.*\bon\s+top\b',
            r'\bsprinkle\b.*\bnutmeg\b',
            r'\btop\s+with\b',
            r'\bdecorate\s+with\b',
            r'\bfinish\s+with\b',
            r'\bfloat\b.*\bon\s+(?:the\s+)?top\b',
            r'\bsqueeze\b.*\bonto\s+the\s+drink\b',
            r'\badd\b.*\bas\s+garnish\b',
            r'\bserve\s+with\b.*\bgarnish\b',
            r'\bsprinkle\b.*\bfresh\b',
            r'\bon\s+top\b'
        ]
        
        for sentence in sentences:
            sentence = sentence.strip()
            if not sentence:
                continue
                
            # Check if this sentence is about garnish
            is_garnish = False
            for pattern in garnish_indicators:
                if re.search(pattern, sentence, re.IGNORECASE):
                    is_garnish = True
                    break
            
            if is_garnish:
                garnish_sentences.append(sentence)
            else:
                # Check if sentence contains preparation verbs
                prep_verbs = [
                    r'\bpour\b', r'\badd\b', r'\bmix\b', r'\bshake\b', r'\bstir\b', 
                    r'\bstrain\b', r'\bbuild\b', r'\bmuddle\b', r'\bplace\b',
                    r'\bfill\b', r'\bblend\b', r'\bcombine\b'
                ]
                
                has_prep_verb = any(re.search(verb, sentence, re.IGNORECASE) for verb in prep_verbs)
                
                # If it has prep verbs or doesn't look like garnish, it's method
                if has_prep_verb or not any(word in sentence.lower() for word in ['garnish', 'sprinkle', 'top', 'decorate']):
                    method_sentences.append(sentence)
                else:
                    # If unsure, check context - if it mentions preparation actions, it's method
                    if any(word in sentence.lower() for word in ['glass', 'ice', 'shaker', 'ingredients']):
                        method_sentences.append(sentence)
                    else:
                        garnish_sentences.append(sentence)
        
        # Reconstruct method and garnish
        method = ' '.join(method_sentences).strip()
        garnish = ' '.join(garnish_sentences).strip()
        
        # Clean up method - ensure it ends with a period
        if method and not method.endswith('.'):
            method += '.'
        
        # Clean up garnish - ensure it starts with capital and ends with period
        if garnish:
            garnish = garnish[0].upper() + garnish[1:] if len(garnish) > 1 else garnish.upper()
            if not garnish.endswith('.'):
                garnish += '.'
        
        return method, garnish
    
    def extract_cocktail_details(self, cocktail_url: str) -> dict:
        """Extract detailed information from individual cocktail page"""
        try:
            response = self.session.get(cocktail_url, timeout=10)
            response.raise_for_status()
            
            soup = BeautifulSoup(response.content, 'html.parser')
            
            details = {
                "ingredients": [],
                "instructions": "",
                "garnish": "",
                "glass": "",
                "method": "",
                "video_url": ""  # Added video URL field
            }
            
            # Extract YouTube video link
            for a in soup.find_all('a', href=True):
                href = a['href']
                if 'youtube.com/watch' in href or 'youtu.be/' in href:
                    # Make sure it's not a channel link
                    if '/watch' in href or 'youtu.be/' in href:
                        details["video_url"] = href
                        break
            
            # Find ingredients in various possible structures
            ingredients_found = False
            
            # Look for ingredients in elementor shortcode divs
            shortcode_divs = soup.find_all('div', class_='elementor-shortcode')
            for div in shortcode_divs:
                ul_element = div.find('ul')
                if ul_element:
                    ingredients = []
                    for li in ul_element.find_all('li'):
                        ingredient_text = li.get_text(strip=True)
                        if ingredient_text and ingredient_text not in ingredients:
                            ingredients.append(ingredient_text)
                    
                    if ingredients:
                        details["ingredients"] = ingredients
                        ingredients_found = True
                        break
            
            # If not found, try other common patterns
            if not ingredients_found:
                # Look for any ul with ingredients
                for ul in soup.find_all('ul'):
                    ingredients = []
                    for li in ul.find_all('li'):
                        text = li.get_text(strip=True)
                        # Check if this looks like an ingredient (contains ml, cl, oz, or common ingredient words)
                        if any(unit in text.lower() for unit in ['ml', 'cl', 'oz', 'dash', 'splash', 'drop']):
                            ingredients.append(text)
                    
                    if len(ingredients) >= 2:  # At least 2 ingredients
                        details["ingredients"] = ingredients
                        break
            
            # Find instructions/method in various structures
            instructions_found = False
            instruction_parts = []
            
            # Look in shortcode divs first - collect all instruction-related parts
            for div in shortcode_divs:
                # Skip divs that contain ingredients (ul elements)
                if div.find('ul'):
                    continue
                
                text_content = div.get_text(strip=True)
                
                # Check if this looks like instructions (preparation or garnish)
                if len(text_content) > 10 and (
                    any(word in text_content.lower() for word in ['pour', 'add', 'shake', 'stir', 'mix', 'strain', 'build', 'muddle']) or
                    any(word in text_content.lower() for word in ['garnish', 'sprinkle', 'top', 'decorate', 'float', 'squeeze'])
                ):
                    instruction_parts.append(text_content)
                    instructions_found = True
            
            # Combine all instruction parts
            if instruction_parts:
                details["instructions"] = " ".join(instruction_parts)
            
            # If not found in shortcode, look in other elements
            if not instructions_found:
                for p in soup.find_all('p'):
                    text = p.get_text(strip=True)
                    if len(text) > 20 and any(word in text.lower() for word in ['pour', 'add', 'shake', 'stir', 'mix', 'strain']):
                        details["instructions"] = text
                        break
            
            # Separate method and garnish
            if details["instructions"]:
                method, garnish = self.separate_method_garnish_improved(details["instructions"])
                details["method"] = method
                details["garnish"] = garnish
            
            # Try to extract glass type from instructions
            glass_keywords = {
                'cocktail glass': 'Cocktail Glass',
                'chilled cocktail glass': 'Chilled Cocktail Glass',
                'old fashioned glass': 'Old Fashioned Glass',
                'highball glass': 'Highball Glass',
                'rocks glass': 'Rocks Glass',
                'martini glass': 'Martini Glass',
                'coupe glass': 'Coupe Glass',
                'champagne flute': 'Champagne Flute',
                'wine glass': 'Wine Glass',
                'shot glass': 'Shot Glass'
            }
            
            instructions_lower = details["instructions"].lower()
            for keyword, glass_name in glass_keywords.items():
                if keyword in instructions_lower:
                    details["glass"] = glass_name
                    break
            
            return details
            
        except Exception as e:
            print(f"Error extracting details from {cocktail_url}: {e}")
            return {
                "ingredients": [],
                "instructions": "",
                "garnish": "",
                "glass": "",
                "method": "",
                "video_url": ""
            }

    def add_details_to_existing_cocktails(self, input_file: str, output_file: str = None, limit: int = None):
        """Add detailed information to existing cocktail list"""
        
        # Load existing basic cocktail data
        with open(input_file, 'r', encoding='utf-8') as f:
            basic_cocktails = json.load(f)
        
        if isinstance(basic_cocktails, dict) and 'cocktails' in basic_cocktails:
            basic_cocktails = basic_cocktails['cocktails']
        
        # Limit cocktails for testing if specified
        if limit:
            basic_cocktails = basic_cocktails[:limit]
            print(f"Adding detailed information to first {len(basic_cocktails)} cocktails (limited for testing)...")
        else:
            print(f"Adding detailed information to {len(basic_cocktails)} cocktails...")
        
        detailed_cocktails = []
        
        for i, cocktail in enumerate(basic_cocktails, 1):
            print(f"[{i}/{len(basic_cocktails)}] Getting details for: {cocktail['title']}")
            
            # Get detailed information from the individual page
            details = self.extract_cocktail_details(cocktail['cocktail_url'])
            
            # Combine basic info with details
            detailed_cocktail = {
                **cocktail,  # Basic info (title, image_url, category, views, etc.)
                **details    # Detailed info (ingredients, instructions, etc.)
            }
            
            detailed_cocktails.append(detailed_cocktail)
            
            # Rate limiting
            time.sleep(0.3)
        
        # Save detailed cocktails
        if not output_file:
            output_file = input_file.replace('.json', '_detailed.json')
        
        with open(output_file, 'w', encoding='utf-8') as f:
            json.dump(detailed_cocktails, f, indent=2, ensure_ascii=False)
        
        print(f"Saved detailed cocktails to {output_file}")
        
        # Show statistics
        with_ingredients = sum(1 for c in detailed_cocktails if c.get('ingredients'))
        with_instructions = sum(1 for c in detailed_cocktails if c.get('instructions'))
        
        print(f"\nDetail Extraction Results:")
        print(f"Cocktails with ingredients: {with_ingredients}/{len(detailed_cocktails)} ({with_ingredients/len(detailed_cocktails)*100:.1f}%)")
        print(f"Cocktails with instructions: {with_instructions}/{len(detailed_cocktails)} ({with_instructions/len(detailed_cocktails)*100:.1f}%)")
        
        return detailed_cocktails


def enhance_existing_cocktails(input_file: str, output_file: str = None) -> dict:
    """Enhanced version of cocktails with mobile app optimizations"""
    
    # Load the detailed cocktails
    with open(input_file, 'r', encoding='utf-8') as f:
        cocktails = json.load(f)
    
    if isinstance(cocktails, dict) and 'cocktails' in cocktails:
        cocktails = cocktails['cocktails']
    
    print(f"Enhancing {len(cocktails)} cocktails for mobile app...")
    
    # Collect all unique values for enums
    categories = set()
    all_ingredients = set()
    ingredient_categories = {
        'spirits': set(),
        'liqueurs': set(), 
        'mixers': set(),
        'garnishes': set(),
        'bitters': set(),
        'syrups': set(),
        'juices': set(),
        'other': set()
    }
    
    enhanced_cocktails = []
    
    # Create a mapping from enum keys to ingredient names for search text generation
    enum_to_name = {}
    
    for cocktail in cocktails:
        # Create enhanced version
        enhanced = cocktail.copy()
        
        # Add category enum
        category = cocktail.get('category', '').lower().replace(' ', '_').replace("'", "")
        enhanced['category_enum'] = category
        categories.add(cocktail.get('category', ''))
        
        # Process ingredients
        ingredients = cocktail.get('ingredients', [])
        ingredients_enums = []
        primary_spirits = []
        
        for ingredient in ingredients:
            # Extract ingredient name (remove measurements)
            ingredient_name = re.sub(r'^\d+(\.\d+)?\s*(ml|cl|oz|dash|splash|drop|pinch)\s*', '', ingredient, flags=re.IGNORECASE).strip()
            ingredient_name = ingredient_name.replace('  ', ' ')
            
            if ingredient_name:
                # Title case the ingredient name
                ingredient_name = ' '.join(word.capitalize() for word in ingredient_name.split())
                
                # Create enum key (lowercase, replace spaces and special chars with underscores)
                enum_key = re.sub(r'[^\w]+', '_', ingredient_name.lower()).strip('_')
                
                ingredients_enums.append(enum_key)
                enum_to_name[enum_key] = ingredient_name
                
                all_ingredients.add(ingredient_name)
                
                # Categorize ingredient
                ingredient_lower = ingredient_name.lower()
                if any(spirit in ingredient_lower for spirit in ['vodka', 'gin', 'rum', 'whiskey', 'bourbon', 'tequila', 'cognac', 'brandy']):
                    ingredient_categories['spirits'].add(ingredient_name)
                    # Check if it's a primary spirit (usually 30ml or more)
                    amount_match = re.match(r'(\d+(?:\.\d+)?)', ingredient)
                    if amount_match and float(amount_match.group(1)) >= 30:
                        primary_spirits.append(ingredient)
                elif any(liqueur in ingredient_lower for liqueur in ['liqueur', 'creme', 'kahlua', 'cointreau', 'grand marnier']):
                    ingredient_categories['liqueurs'].add(ingredient_name)
                elif any(syrup in ingredient_lower for syrup in ['syrup', 'honey']):
                    ingredient_categories['syrups'].add(ingredient_name)
                elif any(juice in ingredient_lower for juice in ['juice', 'lime', 'lemon', 'orange']):
                    ingredient_categories['juices'].add(ingredient_name)
                elif any(mixer in ingredient_lower for mixer in ['water', 'soda', 'tonic', 'ginger', 'cola']):
                    ingredient_categories['mixers'].add(ingredient_name)
                elif any(bitter in ingredient_lower for bitter in ['bitter', 'angostura']):
                    ingredient_categories['bitters'].add(ingredient_name)
                elif any(garnish in ingredient_lower for garnish in ['olive', 'cherry', 'twist', 'peel', 'mint']):
                    ingredient_categories['garnishes'].add(ingredient_name)
                else:
                    ingredient_categories['other'].add(ingredient_name)
        
        enhanced['ingredients_enums'] = ingredients_enums
        enhanced['primary_spirits'] = primary_spirits
        
        # Add search text
        search_text = f"{cocktail.get('title', '').lower()} {category} {' '.join([enum_to_name.get(enum_key, enum_key) for enum_key in ingredients_enums]).lower()} {cocktail.get('instructions', '').lower()}"
        enhanced['search_text'] = search_text
        
        # Add complexity rating
        ingredient_count = len(ingredients)
        if ingredient_count <= 3:
            enhanced['complexity'] = 'simple'
        elif ingredient_count <= 5:
            enhanced['complexity'] = 'medium'
        else:
            enhanced['complexity'] = 'complex'
        
        # Add alcohol strength
        spirit_count = len(primary_spirits)
        if spirit_count == 0:
            enhanced['alcohol_strength'] = 'non_alcoholic'
        elif spirit_count == 1:
            enhanced['alcohol_strength'] = 'light'
        elif spirit_count == 2:
            enhanced['alcohol_strength'] = 'medium'
        else:
            enhanced['alcohol_strength'] = 'strong'
        
        enhanced_cocktails.append(enhanced)
    
    # Create mobile app structure with enums
    mobile_app_data = {
        "metadata": {
            "total_cocktails": len(enhanced_cocktails),
            "source": "IBA World Cocktails",
            "includes_details": True,
            "includes_enums": True,
            "version": "2.0",
            "enhanced_from": input_file
        },
        "enums": {
            "categories": [
                {
                    "key": re.sub(r'[^\w]+', '_', cat.lower()).strip('_'),
                    "value": cat
                } 
                for cat in sorted(categories) if cat
            ],
            "ingredients": {
                "all_ingredients": [
                    {
                        "key": re.sub(r'[^\w]+', '_', ing.lower()).strip('_'),
                        "value": ing
                    } 
                    for ing in sorted(all_ingredients) if ing
                ],
                "by_category": {
                    cat: [
                        {
                            "key": re.sub(r'[^\w]+', '_', ing.lower()).strip('_'),
                            "value": ing
                        }
                        for ing in sorted(ingredients) if ing
                    ]
                    for cat, ingredients in ingredient_categories.items()
                    if ingredients
                }
            },
            "complexity_levels": [
                {"key": "simple", "value": "Simple"},
                {"key": "medium", "value": "Medium"}, 
                {"key": "complex", "value": "Complex"}
            ],
            "alcohol_strengths": [
                {"key": "non_alcoholic", "value": "Non-Alcoholic"},
                {"key": "light", "value": "Light"},
                {"key": "medium", "value": "Medium"},
                {"key": "strong", "value": "Strong"}
            ]
        },
        "cocktails": enhanced_cocktails
    }
    
    # Save enhanced data
    if not output_file:
        output_file = input_file.replace('.json', '_mobile_app.json')
    
    with open(output_file, 'w', encoding='utf-8') as f:
        json.dump(mobile_app_data, f, indent=2, ensure_ascii=False)
    
    print(f"Enhanced mobile app data saved to {output_file}")
    return mobile_app_data


def create_complete_mobile_app_json(
    basic_cocktails_file: str = "iba_cocktails.json",
    detailed_cocktails_file: str = None,
    final_mobile_app_file: str = "iba_cocktails_complete_mobile_app.json",
    fetch_details: bool = True,
    limit: int = None
):
    """Create complete mobile app JSON with all features"""
    
    print("🍸 Creating Complete Mobile App JSON")
    print("=" * 50)
    
    # Step 1: Check if we already have detailed cocktails
    if not detailed_cocktails_file:
        detailed_cocktails_file = basic_cocktails_file.replace('.json', '_detailed.json')
    
    if fetch_details:
        print("Step 1: Adding detailed information to cocktails...")
        parser = CompleteIBACocktailParser()
        detailed_cocktails = parser.add_details_to_existing_cocktails(
            basic_cocktails_file, 
            detailed_cocktails_file,
            limit=limit
        )
    else:
        print("Step 1: Using existing detailed cocktails file...")
        try:
            with open(detailed_cocktails_file, 'r', encoding='utf-8') as f:
                detailed_cocktails = json.load(f)
        except FileNotFoundError:
            print(f"Error: {detailed_cocktails_file} not found. Set fetch_details=True to create it.")
            return
    
    print(f"\nStep 2: Enhancing with mobile app enum fields...")
    
    # Step 2: Enhance with enum fields
    enhanced_data = enhance_existing_cocktails(detailed_cocktails_file, final_mobile_app_file)
    
    print(f"\n✅ Complete Mobile App JSON created: {final_mobile_app_file}")
    print(f"\n📊 Final Statistics:")
    print(f"   Total cocktails: {enhanced_data['metadata']['total_cocktails']}")
    print(f"   Categories: {len(enhanced_data['enums']['categories']['display_names'])}")
    print(f"   Unique ingredients: {len(enhanced_data['enums']['ingredients']['all_ingredients'])}")
    
    ingredient_categories = enhanced_data['enums']['ingredients']['by_category']
    print(f"   Ingredient categories: {len(ingredient_categories)}")
    for cat, ingredients in ingredient_categories.items():
        print(f"     {cat.capitalize()}: {len(ingredients)} ingredients")
    
    return enhanced_data


def main():
    import sys
    
    # Check command line arguments for different modes
    test_mode = '--test' in sys.argv or '--test-10' in sys.argv
    
    print("🍸 IBA COCKTAILS COMPLETE PARSER WITH VIDEO LINKS")
    print("=" * 60)
    print("This script will extract ALL IBA cocktails with:")
    print("✅ Complete ingredient lists and preparation methods")
    print("✅ YouTube video links for each cocktail")  
    print("✅ Mobile app optimized enum structures")
    print("✅ Category and complexity classifications")
    print("✅ Search and filter optimization")
    print()
    
    if test_mode:
        print("🧪 TEST MODE: Processing first 10 cocktails only")
        limit = 10
        max_pages = 1
        basic_file = "iba_cocktails_test.json"
        final_file = "iba_cocktails_test_complete.json"
    else:
        print("🚀 PRODUCTION MODE: Processing ALL IBA cocktails")
        limit = None
        max_pages = 20  # Should be enough to get all cocktails
        basic_file = "iba_cocktails_all.json"
        final_file = "iba_cocktails_complete.json"
    
    try:
        # Step 1: Extract all basic cocktail information
        print(f"\nStep 1: Extracting basic cocktail information...")
        parser = CompleteIBACocktailParser()
        all_cocktails = parser.extract_all_cocktails(max_pages=max_pages)
        
        if not all_cocktails:
            print("❌ No cocktails found!")
            return
        
        # Limit for testing if needed
        if test_mode and len(all_cocktails) > 10:
            all_cocktails = all_cocktails[:10]
            print(f"   🧪 Limited to first 10 cocktails for testing")
        
        # Save basic cocktails
        with open(basic_file, 'w', encoding='utf-8') as f:
            json.dump(all_cocktails, f, indent=2, ensure_ascii=False)
        print(f"   ✅ Basic cocktails saved to {basic_file}")
        
        # Step 2: Add detailed information (including video links)
        print(f"\nStep 2: Adding detailed information and video links...")
        print(f"   This will fetch each cocktail page - may take several minutes...")
        
        detailed_file = basic_file.replace('.json', '_detailed.json')
        detailed_cocktails = parser.add_details_to_existing_cocktails(basic_file, detailed_file, limit=limit)
        
        # Step 3: Create mobile app optimized version
        print(f"\nStep 3: Creating mobile app optimized JSON...")
        enhanced_data = enhance_existing_cocktails(detailed_file, final_file)
        
        # Success summary
        print(f"\n🎉 SUCCESS! Complete IBA cocktails database created!")
        print(f"=" * 50)
        print(f"📱 Final file: {final_file}")
        print(f"🍸 Total cocktails: {len(enhanced_data['cocktails'])}")
        
        # Count videos
        video_count = sum(1 for c in enhanced_data['cocktails'] if c.get('video_url'))
        print(f"🎥 Videos included: {video_count}/{len(enhanced_data['cocktails'])} cocktails ({video_count/len(enhanced_data['cocktails'])*100:.1f}%)")
        
        # Show enum counts
        enums = enhanced_data['enums']
        print(f"📂 Categories: {len(enums['categories'])} types")
        print(f"🧪 Ingredients: {len(enums['ingredients']['all_ingredients'])} unique types")
        
        print(f"\n✅ Your complete cocktail database is ready!")
        print(f"   • Full recipe details with method/garnish separation")
        print(f"   • YouTube tutorial videos")
        print(f"   • Mobile app enum structures")
        print(f"   • Search and filter optimization")
        
        if test_mode:
            print(f"\n💡 To process ALL cocktails, run: python3 complete_mobile_app_parser.py")
                
    except KeyboardInterrupt:
        print(f"\n⏹️  Process interrupted by user")
    except Exception as e:
        print(f"\n❌ Error: {e}")
        import traceback
        traceback.print_exc()


if __name__ == "__main__":
    main()
