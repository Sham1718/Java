import React, { useState } from "react";
import { getByTitle } from "../service/noteService";
import NoteCard from "./NoteCard";

const Navbar = () => {
  const [title, setTitle] = useState("");
  const [searched, setSearched] = useState(false);
  const [result, setResult] = useState(null);

  const handleSearch = async () => {
    try {
      const response = await getByTitle(title);

      setResult(response.data);
      setSearched(true);
    } catch (error) {
      setResult(null);
      setSearched(true);
      console.error(error);
    }
  };

  return (
    <>
      <nav className="bg-white border-b border-gray-200 shadow-sm sticky top-0 z-50">
        <div className="max-w-7xl mx-auto px-6 py-4 flex flex-col md:flex-row items-center justify-between gap-4">

          {/* Logo */}
          <div>
            <h1 className="text-3xl font-bold tracking-tight text-slate-800">
              Notes
              <span className="text-amber-600">Manager</span>
            </h1>

            <p className="text-sm text-gray-500 mt-1">
              Organize your notes efficiently
            </p>
          </div>

          {/* Search */}
          <div className="flex w-full md:w-auto gap-3">

            <input
              type="text"
              placeholder="Search by title..."
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              className="w-full md:w-80 px-4 py-2.5 rounded-lg border border-gray-300 outline-none
              focus:border-slate-700 transition-all"
            />

            <button
              onClick={handleSearch}
              className="bg-slate-800 hover:bg-slate-900 text-white px-6 py-2.5 rounded-lg font-medium transition-all"
            >
              Search
            </button>

          </div>
        </div>
      </nav>

      {/* Search Result */}

      {searched && (
        <section className="max-w-7xl mx-auto px-6 mt-8">

          <h2 className="text-lg font-semibold text-slate-700 mb-4">
            Search Result
          </h2>

          {result ? (
            <div className="max-w-md">
              <NoteCard note={result} />
            </div>
          ) : (
            <div className="bg-white border border-gray-200 rounded-lg p-8 text-center shadow-sm">
              <h3 className="text-xl font-semibold text-gray-700">
                No Note Found
              </h3>

              <p className="text-gray-500 mt-2">
                Try searching with another title.
              </p>
            </div>
          )}

        </section>
      )}
    </>
  );
};

export default Navbar;