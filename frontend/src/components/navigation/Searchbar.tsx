import { useState, useEffect, useRef } from "react";

interface SearchbarProps {
  onSearch: (searchTerm: string) => void;
}

export default function Searchbar({ onSearch }: SearchbarProps) {
  const [searchTerm, setSearchTerm] = useState("");
  const timeoutRef = useRef<number | undefined>(undefined);

  useEffect(() => {
    // Clear previous timeout
    if (timeoutRef.current) {
      clearTimeout(timeoutRef.current);
    }

    // Set new timeout
    timeoutRef.current = window.setTimeout(() => {
      onSearch(searchTerm);
    }, 300);

    // Cleanup function
    return () => {
      if (timeoutRef.current) {
        clearTimeout(timeoutRef.current);
      }
    };
  }, [searchTerm, onSearch]);

  const handleClear = () => {
    setSearchTerm("");
  };

  return (
    <div className="searchbar">
      <input
        type="text"
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        placeholder="Search..."
      />
      {searchTerm && (
        <button onClick={handleClear}>Clear</button>
      )}
    </div>
  );
}