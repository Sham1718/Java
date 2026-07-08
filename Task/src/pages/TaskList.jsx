import React, { useEffect, useState } from "react";
import { getAllTask } from "../utils/service";
import TaskCard from "../Components/TaskCard";

const TaskList = ({searchResult}) => {
  const [tasks, setTasks] = useState([]);


  const fetchTask = async () => {
    try {
      const response = await getAllTask();
      setTasks(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  const displayTask =searchResult ? [searchResult] :tasks;

  console.log(searchResult);

  useEffect(() => {
    fetchTask();
  }, []);

  return (
    <div className="min-h-screen bg-gray-100 py-10 px-5">
      <div className="max-w-6xl mx-auto">
        <h1 className="text-4xl font-bold text-center mb-8 text-gray-800">
          Task List
        </h1>

        {displayTask.length === 0 ? (
          <div className="text-center text-gray-500 text-lg">
            No Tasks Found
          </div>
        ) : (
          <div className="grid gap-6 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3">
            {displayTask.map((task) => (
              <TaskCard key={task.id} task={task} />
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default TaskList;