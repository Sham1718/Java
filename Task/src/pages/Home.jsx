import React, { useState } from "react";
import Navbar from "../Components/Navbar";
import TaskList from "./TaskList";

const Home = () => {
  const [search, setSearch] = useState("");
  const [searchResult, setSearchResult] = useState(null);

  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar search={search} setSearch={setSearch} setSearchResult={setSearchResult}/>

      <div className="max-w-7xl mx-auto p-6">
        <TaskList searchResult={searchResult} />
      </div>
    </div>
  );
};

export default Home;