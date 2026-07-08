import React, { useState } from "react";
import { createTask } from "../utils/service";
import { useNavigate } from "react-router-dom";

const AddTask = () => {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [complete, setComplete] = useState(false);
  const navigate =useNavigate();

  const handleSubmit = async () => {
    try {
      console.log({ title, description, complete });

      await createTask({
        title,
        description,
        complete,
      });

      setTitle("");
      setDescription("");
      setComplete(false);
      navigate("/")

    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="w-full max-w-md bg-white shadow-lg rounded-xl p-6">

        <h2 className="text-2xl font-bold text-center mb-6">
          Add New Task
        </h2>

        {/* Title */}
        <div className="mb-4">
          <label className="block mb-2 font-medium">Title</label>
          <input
            type="text"
            placeholder="Enter task title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            className="w-full border rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        {/* Description */}
        <div className="mb-4">
          <label className="block mb-2 font-medium">
            Description
          </label>

          <textarea
            rows="4"
            placeholder="Enter task description"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            className="w-full border rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        {/* Status */}
        <div className="mb-6">
          <label className="block mb-2 font-medium">
            Status
          </label>

          <select
            value={complete}
            onChange={(e) =>
              setComplete(e.target.value === "true")
            }
            className="w-full border rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="false">Pending</option>
            <option value="true">Completed</option>
          </select>
        </div>

        {/* Button */}
        <button
          onClick={handleSubmit}
          className="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition duration-300"
        >
          Submit Task
        </button>

      </div>
    </div>
  );
};

export default AddTask;