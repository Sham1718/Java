import React, { useEffect, useState } from "react";
import { getTaskById, updateTask } from "../utils/service";
import { useNavigate, useParams } from "react-router-dom";

const EditTask = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [complete, setComplete] = useState(false);

  const fetchTask = async () => {
    try {
      const res = await getTaskById(id);

      setTitle(res.data.title);
      setDescription(res.data.description);
      setComplete(res.data.complete);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    fetchTask();
  }, []);

  const handleSubmit = async () => {
    try {
      await updateTask(id, {
        title,
        description,
        complete,
      });

      alert("Task Updated Successfully!");

      navigate("/");
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 flex justify-center items-center px-4">
      <div className="w-full max-w-md bg-white shadow-lg rounded-xl p-8">

        <h2 className="text-3xl font-bold text-center mb-8">
          Edit Task
        </h2>

        {/* Title */}
        <div className="mb-5">
          <label className="block mb-2 font-medium">
            Title
          </label>

          <input
            type="text"
            value={title}
            placeholder="Task title"
            onChange={(e) => setTitle(e.target.value)}
            className="w-full border rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        {/* Description */}
        <div className="mb-5">
          <label className="block mb-2 font-medium">
            Description
          </label>

          <textarea
            rows="4"
            value={description}
            placeholder="Task description"
            onChange={(e) => setDescription(e.target.value)}
            className="w-full border rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        {/* Status */}
        <div className="mb-8">
          <label className="block mb-2 font-medium">
            Status
          </label>

          <select
            value={complete}
            onChange={(e) => setComplete(e.target.value === "true")}
            className="w-full border rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="false">Pending</option>
            <option value="true">Completed</option>
          </select>
        </div>

        {/* Buttons */}
        <div className="flex gap-4">

          <button
            onClick={() => navigate(-1)}
            className="flex-1 border border-gray-300 rounded-lg py-2 hover:bg-gray-100 transition"
          >
            Cancel
          </button>

          <button
            onClick={handleSubmit}
            className="flex-1 bg-blue-600 text-white rounded-lg py-2 hover:bg-blue-700 transition"
          >
            Update Task
          </button>

        </div>

      </div>
    </div>
  );
};

export default EditTask;