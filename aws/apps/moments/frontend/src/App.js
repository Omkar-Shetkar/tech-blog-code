import React, { useState, useEffect } from "react";
import "./App.css";

const API_BASE = "https://f83szrkvsi.execute-api.us-east-1.amazonaws.com";

function groupPhotosByDate(photos) {
  return photos.reduce((groups, photo) => {
    const date = new Date(photo.uploadDate);
    const dayKey = date.toDateString(); // e.g. "Sat Nov 29 2025"
    if (!groups[dayKey]) {
      groups[dayKey] = [];
    }
    groups[dayKey].push(photo);
    return groups;
  }, {});
}

function App() {
  const [activeTab, setActiveTab] = useState("upload");
  const [selectedFile, setSelectedFile] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [message, setMessage] = useState("");

  const [photos, setPhotos] = useState([]);
  const [loadingPhotos, setLoadingPhotos] = useState(false);
  const [photoError, setPhotoError] = useState("");

  useEffect(() => {
    if (activeTab !== "view") return;

    const fetchPhotos = async () => {
      setLoadingPhotos(true);
      setPhotoError("");
      try {
        const response = await fetch(`${API_BASE}/api/photos`);
        if (!response.ok) {
          throw new Error("Failed to load photos");
        }
        const data = await response.json();
        setPhotos(data);
      } catch (err) {
        setPhotoError("Could not load photos. Please try again.");
      } finally {
        setLoadingPhotos(false);
      }
    };

    fetchPhotos();
  }, [activeTab]);

  const handleFileChange = (event) => {
    const file = event.target.files && event.target.files[0];
    setSelectedFile(file || null);
    setMessage("");
  };

  const handleUploadClick = async () => {
    if (!selectedFile) return;

    setUploading(true);
    setMessage("");

    try {
      const formData = new FormData();
      formData.append("file", selectedFile);

      const response = await fetch(`${API_BASE}/api/uploads`, {
        method: "POST",
        body: formData,
        // No manual Content-Type header; browser sets multipart boundary
      });

      if (!response.ok) {
        throw new Error("Upload failed");
      }

      console.log("Upload response:", response);
      const data = await response.json();

      console.log("Upload data:", data);
      
      setMessage(`Uploaded successfully. Key: ${data.key}`);
      setSelectedFile(null);
    } catch (err) {
      setMessage("Upload failed. Please try again.");
    } finally {
      setUploading(false);
    }
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
                disabled={!selectedFile || uploading}
                onClick={handleUploadClick}
              >
                {uploading ? "Uploading..." : "Upload"}
              </button>

              {message && <p className="upload-message">{message}</p>}
            </div>
          )}

          {activeTab === "view" && (
            <div className="view-screen">
              <h2>View Moments</h2>

              {loadingPhotos && <p>Loading photos...</p>}
              {photoError && <p className="error-text">{photoError}</p>}

              {!loadingPhotos && !photoError && photos.length === 0 && (
                <p>No photos uploaded yet.</p>
              )}

              {!loadingPhotos && !photoError && photos.length > 0 && (
                <>
                  {Object.entries(groupPhotosByDate(photos)).map(
                    ([date, items]) => (
                      <div key={date} className="photo-group">
                        <h3 className="photo-group-title">{date}</h3>
                        <div className="photo-grid">
                          {items.map((photo) => (
                            <div key={photo.id} className="photo-item">
                              <img
                                src={photo.url}
                                alt={photo.description || "Moment"}
                              />
                            </div>
                          ))}
                        </div>
                      </div>
                    )
                  )}
                </>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default App;
