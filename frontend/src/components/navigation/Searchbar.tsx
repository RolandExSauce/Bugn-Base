export default function Searchbar() {
  return (
    <div className="searchbar flex-grow-1">
      <input
        placeholder="Produkte durchsuchen..."
        type="text"
        className="searchbar-input w-100 px-3"
      />
    </div>
  );
}
