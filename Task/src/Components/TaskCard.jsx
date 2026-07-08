import React from "react";
import { useNavigate } from "react-router-dom";

const TaskCard = ({ task }) => {
  const navigate = useNavigate();

  return (
    <div className="bg-white border border-gray-200 rounded-xl shadow-sm hover:shadow-lg hover:-translate-y-1 transition-all duration-300 p-6 flex flex-col justify-between">

      {/* Header */}
      <div className="flex items-start justify-between mb-4">
        <h2 className="text-xl font-bold text-slate-800 wrap-break-words">
          {task.title}
        </h2>

        <span
          className={`px-3 py-1 rounded-full text-xs font-semibold ${
            task.complete
              ? "bg-green-100 text-green-700"
              : "bg-yellow-100 text-yellow-700"
          }`}
        >
          {task.complete ? "Completed" : "Pending"}
        </span>
      </div>

      {/* Description */}
      <p className="text-gray-600 leading-7 mb-6">
        {task.description}
      </p>

      {/* Footer */}
      <div className="flex items-center justify-between text-sm text-gray-500 border-t pt-4">
        <div>
          <p>
            <span className="font-medium">Task ID:</span> #{task.id}
          </p>

          <p className="mt-1">
            {new Date(task.createdAT).toLocaleDateString()}
          </p>
        </div>

        <button
          onClick={() => navigate(`/task/${task.id}`)}
          className="px-4 py-2 rounded-lg border border-slate-300 hover:bg-slate-100 transition"
        >
          View
        </button>
      </div>
    </div>
  );
};

export default TaskCard;