import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { deleteTask, getTaskById } from "../utils/service";

const TaskDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [task, setTask] = useState(null);

  const fetchTask = async () => {
    try {
      const res = await getTaskById(id);
      setTask(res.data);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    fetchTask();
  }, []);

  const handleDelete = async () => {
    const confirmDelete = window.confirm(
      "Are you sure you want to delete this task?"
    );

    if (!confirmDelete) return;

    try {
      await deleteTask(id);
      alert("Task Deleted Successfully");
      navigate("/");
    } catch (error) {
      console.log(error);
    }
  };

  if (!task) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-100">
        Loading...
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-100 flex justify-center items-center px-4">
      <div className="bg-white shadow-lg rounded-xl p-8 w-full max-w-xl">

        <h1 className="text-3xl font-bold mb-6">
          Task Details
        </h1>

        <div className="mb-5">
          <h2 className="font-semibold">Title</h2>
          <p>{task.title}</p>
        </div>

        <div className="mb-5">
          <h2 className="font-semibold">Description</h2>
          <p>{task.description}</p>
        </div>

        <div className="mb-8">
          <h2 className="font-semibold">Status</h2>

          {task.complete ? (
            <span className="bg-green-100 text-green-700 px-4 py-1 rounded-full">
              Completed
            </span>
          ) : (
            <span className="bg-yellow-100 text-yellow-700 px-4 py-1 rounded-full">
              Pending
            </span>
          )}
        </div>

        {/* Buttons */}
        <div className="flex gap-4">

          <button
            onClick={() => navigate(`/edit/${id}`)}
            className="flex-1 bg-blue-600 hover:bg-blue-700 text-white py-2 rounded-lg transition"
          >
            Edit
          </button>

          <button
            onClick={handleDelete}
            className="flex-1 bg-red-600 hover:bg-red-700 text-white py-2 rounded-lg transition"
          >
            Delete
          </button>

        </div>

      </div>
    </div>
  );
};

export default TaskDetails;