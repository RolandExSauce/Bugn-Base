export default function Searchbar() {
  return (
    <div className="searchbar flex-grow-1">
      <input
        placeholder="Search for products"
        type="text"
        className="searchbar-input w-100"
      />
    </div>
  );
}
