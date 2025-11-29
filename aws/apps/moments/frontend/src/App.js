import React, { useState } from "react";
import "./App.css";

function App() {
  const [activeTab, setActiveTab] = useState("upload");
  const [selectedFile, setSelectedFile] = useState(null);

  const handleFileChange = (event) => {
    const file = event.target.files && event.target.files[0];
    setSelectedFile(file || null);
  };

  return (
    <div className="app-root">
      <div className="app-card">
        <h1 className="app-title">Moments - Place for Memories</h1>

        <div className="tabs">
          <button
            className={`tab ${activeTab === "upload" ? "tab-active" : ""}`}
            onClick={() => setActiveTab("upload")}
          >
            Upload
          </button>
          <button
            className={`tab ${activeTab === "view" ? "tab-active" : ""}`}
            onClick={() => setActiveTab("view")}
          >
            View
          </button>
        </div>

        <div className="tab-content">
          {activeTab === "upload" && (
            <div className="upload-screen">
              <p className="upload-hint">Choose a photo to upload.</p>

              <label className="file-picker">
                <input
                  type="file"
                  accept="image/*"
                  onChange={handleFileChange}
                />
                <span>Browse…</span>
              </label>

              {selectedFile && (
                <div className="file-info">
                  <div>Name: {selectedFile.name}</div>
                  <div>Type: {selectedFile.type || "Unknown"}</div>
                  <div>Size: {(selectedFile.size / 1024).toFixed(1)} KB</div>
                </div>
              )}

              <button
                className="primary-btn"
                disabled={!selectedFile}
                onClick={() => {}}
              >
                Upload
              </button>
            </div>
          )}

          {activeTab === "view" && (
            <div>
              <h2>View Moments</h2>
              <p>Gallery will appear here.</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default App;
