// Searchbar.tsx
import { useState, useEffect, useRef } from "react";

interface SearchbarProps {
  searchTerm: string;
  onSearch: (searchTerm: string) => void;
}

export default function Searchbar({ searchTerm, onSearch }: SearchbarProps) {
  const [inputValue, setInputValue] = useState(searchTerm);
  const timeoutRef = useRef<number | undefined>(undefined);
  const inputRef = useRef<HTMLInputElement>(null);

  const firstMountRef = useRef(true);

  useEffect(() => {
    if (searchTerm === "") setInputValue("");
  }, [searchTerm]);

  useEffect(() => {
    if (firstMountRef.current) {
      firstMountRef.current = false;
      return;
    }

    if (timeoutRef.current) clearTimeout(timeoutRef.current);
    timeoutRef.current = window.setTimeout(() => {
      if (inputValue !== searchTerm) {
        onSearch(inputValue);
      }
    }, 300);

    inputRef.current?.focus();
    return () => {
      if (timeoutRef.current) clearTimeout(timeoutRef.current);
    };
  }, [inputValue, onSearch, searchTerm]);

  return (
    <div className="searchbar">
      <input
        type="text"
        value={inputValue}
        onChange={(e) => setInputValue(e.target.value)}
        placeholder="Search..."
        ref={inputRef}
      />
      {inputValue && <button onClick={() => setInputValue("")}>Clear</button>}
    </div>
  );
}
