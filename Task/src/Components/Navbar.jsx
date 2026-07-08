import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getTaskByTitle } from "../utils/service";

const Navbar = ({ search, setSearch ,setSearchResult}) => {
  const navigate = useNavigate();

  console.log(search);
  const handleSearch = async () => {
  if (search.trim() === "") {
    setSearchResult(null);
    return;
  }

  try {
    const res = await getTaskByTitle(search);
    setSearchResult(res.data);
  } catch (error) {
    console.error(error);
    setSearchResult(null);
  }
};

  return (
    <nav className="bg-blue-600 shadow-md">
      <div className="max-w-7xl mx-auto px-6 py-4 flex flex-col md:flex-row justify-between items-center gap-4">

        <h1
          onClick={() => navigate("/")}
          className="text-3xl font-bold text-white cursor-pointer"
        >
          Task Manager
        </h1>

        <div className="flex gap-3 w-full md:w-auto">

          <input
            type="text"
            placeholder="Search by title..."
            value={search}
            onChange={(e) => { setSearch(e.target.value); if (e.target.value === "") {setSearchResult(null);}}}
            className="bg-white rounded-lg px-4 py-2 w-full md:w-80 focus:outline-none focus:ring-2 focus:ring-white"
          />

          <button onClick={()=>handleSearch()}
            className="bg-white text-blue-600 px-5 py-2 rounded-lg font-semibold hover:bg-gray-100"
            >
            search
          </button>

          <button
            onClick={() => navigate("/create")}
            className="bg-white text-blue-600 px-5 py-2 rounded-lg font-semibold hover:bg-gray-100"
          >
            + Add Task
          </button>

        </div>

      </div>
    </nav>
  );
};

export default Navbar;