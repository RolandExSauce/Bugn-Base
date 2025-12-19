import { useCallback, useEffect, useRef, useState } from "react";
import { AdminImageService } from "../../services";
import type { Image } from "../../types/models";
import { noImgFoundPlaceholder } from "../../assets/icon.barrel";

interface Props {
  productId: string;
}

export default function ProductImagesAdmin({ productId }: Props) {
  const [images, setImages] = useState<Image[]>([]);
  const [loading, setLoading] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [selectedFiles, setSelectedFiles] = useState<File[]>([]);
  const fileInputRef = useRef<HTMLInputElement>(null);

  const fetchImages = useCallback(async () => {
    setLoading(true);
    try {
      const data = await AdminImageService.getProductImages(productId);
      setImages(data);
    } finally {
      setLoading(false);
    }
  }, [productId]);

  useEffect(() => {
    fetchImages();
  }, [fetchImages]);

  const handleFileSelect = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (!event.target.files || event.target.files.length === 0) return;

    const files = Array.from(event.target.files);
    setSelectedFiles(files);

    // Clear the input so the same file can be selected again
    if (fileInputRef.current) {
      fileInputRef.current.value = "";
    }
  };

  const handleUpload = async () => {
    if (selectedFiles.length === 0) return;

    setUploading(true);
    try {
      await AdminImageService.uploadImages(productId, selectedFiles);

      // Wait a moment for the backend to process and write files
      await new Promise((resolve) => setTimeout(resolve, 500));

      // Refresh images
      await fetchImages();

      // Clear selected files
      setSelectedFiles([]);
    } catch (error) {
      console.error("Error uploading images:", error);
      // TODO: Show error message
    } finally {
      setUploading(false);
    }
  };

  const handleCancelUpload = () => {
    setSelectedFiles([]);
  };

  const handleDelete = async (imageId: string) => {
    if (!window.confirm("Bild wirklich löschen?")) return;

    try {
      await AdminImageService.deleteImage(imageId);
      setImages((prev) => prev.filter((i) => i.imageId !== imageId));
    } catch (error) {
      console.error("Error deleting image:", error);
      // TODO: Show error message
    }
  };

  const resolveImageUrl = (url?: string) => {
    if (!url) return noImgFoundPlaceholder;
    return `${import.meta.env.VITE_BASE_URL}${url}`;
  };

  return (
    <div className="border rounded p-3 bg-light">
      <h6 className="mb-3">Produktbilder</h6>

      {/* File Selection */}
      <div className="mb-3">
        <input
          ref={fileInputRef}
          type="file"
          multiple
          accept="image/*"
          className="form-control"
          onChange={handleFileSelect}
        />
      </div>

      {/* Selected Files Preview & Upload Controls */}
      {selectedFiles.length > 0 && (
        <div className="mb-3 p-3 border rounded bg-white">
          <h6 className="mb-2">
            Ausgewählte Dateien ({selectedFiles.length}):
          </h6>
          <ul className="mb-3">
            {selectedFiles.map((file, index) => (
              <li key={index} className="text-muted small">
                {file.name} ({(file.size / 1024).toFixed(1)} KB)
              </li>
            ))}
          </ul>

          <div className="d-flex gap-2">
            <button
              onClick={handleUpload}
              disabled={uploading}
              className="btn btn-sm btn-success"
            >
              {uploading ? (
                <>
                  <span className="spinner-border spinner-border-sm me-1"></span>
                  Wird hochgeladen...
                </>
              ) : (
                <>
                  <i className="bi bi-upload me-1"></i>
                  Hochladen
                </>
              )}
            </button>
            <button
              onClick={handleCancelUpload}
              disabled={uploading}
              className="btn btn-sm btn-secondary"
            >
              <i className="bi bi-x me-1"></i>
              Abbrechen
            </button>
          </div>
        </div>
      )}

      {/* Existing Images */}
      <div className="mt-4">
        <h6>Vorhandene Bilder</h6>
        {loading ? (
          <div className="d-flex align-items-center">
            <div className="spinner-border spinner-border-sm me-2"></div>
            <p className="mb-0">Lade Bilder…</p>
          </div>
        ) : images.length === 0 ? (
          <p className="text-muted">Keine Bilder vorhanden</p>
        ) : (
          <div className="d-flex flex-wrap gap-3">
            {images.map((img) => (
              <div key={img.imageId} className="position-relative">
                <img
                  src={resolveImageUrl(img.url)}
                  alt={img.altText ?? "Produktbild"}
                  style={{
                    width: 120,
                    height: 120,
                    objectFit: "cover",
                    borderRadius: 6,
                  }}
                  onError={(e) => {
                    (e.currentTarget as HTMLImageElement).src =
                      noImgFoundPlaceholder;
                  }}
                />
                <button
                  className="btn btn-sm btn-danger position-absolute top-0 end-0"
                  onClick={() => handleDelete(img.imageId)}
                  title="Bild löschen"
                >
                  ×
                </button>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
