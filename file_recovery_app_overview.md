# 📂 File Recovery App — Product Overview

## 🧭 Overview
The File Recovery App helps users easily find, recover, and manage deleted or duplicate files on their device.  
Its goal is to provide a clear, safe, and intuitive way to regain lost data and keep storage optimized.

The app is divided into three main sections accessible through bottom navigation:
**Recovery**, **History**, and **Settings**.

---

## 🧱 Main Sections

### 🧩 1. Recovery Page

#### Purpose
The Recovery page allows users to quickly start a scan for deleted files and use built-in tools to clean unnecessary data.

#### Structure

##### **Recovery Section**
A grid of buttons for choosing the type of files to recover:
- **Photos**  
- **Videos**  
- **Documents**  
- **All Files**

Each option leads to a dedicated scanning page where users can view and restore deleted files of that type.

##### **Tools Section**
Additional cleanup tools that help keep storage organized:
- **Remove Screenshots** — find and delete old or duplicate screenshots.  
- **Remove Same Files** — detect and remove duplicate files to free up space.

##### **Storage Section**
Shows the current storage usage in a progress bar format.  
The bar is divided by file types (photos, videos, documents, others) to help visualize how space is used.  
Also displays total and available memory.

---

### 🕘 2. History Page

#### Purpose
The History page stores all previously recovered files and allows users to review or permanently delete them.

#### Structure
The page includes **three tabs**:
- **Photos**
- **Videos**
- **Other**

Each tab displays a **grid** of recovered files.  
Users can:
- Select multiple files.
- View file size.
- See a visual indicator for selected items.
- Remove selected items if no longer needed.

---

### ⚙️ 3. Settings Page

#### Purpose
The Settings page provides personalization options and user statistics.

#### Structure
- **Theme** — toggle between light and dark themes.
- **Statistics Section** — summary of app activity:
  - Total number of recovered files.
  - Total recovered memory.
  - Breakdown by file types (photos, videos, documents, others).

---

## 🔍 Scan Files Flow

#### Purpose
This flow allows users to find deleted or lost files in a specific category and choose what to recover.

#### Process

1. **Start Scan**
   - When the user selects a file type (e.g., Photos) from the Recovery page, the app navigates to the Scan Files page.

2. **Scanning Progress**
   - The page shows a visual progress indicator and the number of files found so far.
   - As the scan runs, users see a grid list of discovered files, grouped by date.

3. **Filters**
   - **Filter by Date:**
     - All days  
     - Last 7 days  
     - Last month  
     - Last 6 months
   - **Filter by Size:**
     - All  
     - Less than 1 MB  
     - 1–5 MB  
     - Greater than 5 MB

4. **Selection**
   - Users can select individual files or use “Select All”.
   - Each item shows a thumbnail and file size.

5. **Actions**
   - **Recover** — restore the selected files.
   - **Remove** — permanently delete selected files.

6. **Recovery Progress**
   - While recovering, users see a progress bar and status updates.

7. **Success Screen**
   - After completion, the app shows a success message with two buttons:
     - **Close** — return to the main screen.
     - **Show History** — open the History page to view recovered files.

---

## 🌟 Goals and Value

- Help users **recover accidentally deleted files** with minimal effort.  
- Provide **clear insights into storage usage** and memory optimization.  
- Offer **cleanup tools** to remove duplicates and unnecessary files.  
- Ensure the experience is **intuitive, visual, and reassuring** — users always understand what is happening and why.

---

## 🧠 Guiding Principles

- **Clarity** — simple navigation and meaningful icons.  
- **Trust** — transparent operations and clear confirmations before deletion or recovery.  
- **Efficiency** — quick access to key functions and optimized scanning experience.  
- **Personalization** — adaptable theme and usage statistics for better user awareness.
