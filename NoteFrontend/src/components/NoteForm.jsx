import React, { useState } from "react";
import { createNote } from "../service/noteService";

const NoteForm = ({ onCreate }) => {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");

  const handleSubmit = async () => {
    if (!title.trim() || !description.trim()) {
      alert("Please fill all fields");
      return;
    }

    const note = {
      title,
      description,
    };

    try {
      await createNote(note);

      alert("Note Created Successfully");

      setTitle("");
      setDescription("");

      onCreate();
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <section className="max-w-5xl mx-auto px-6 mt-10">

      <div className="bg-white border border-gray-200 rounded-xl shadow-sm p-8">

        <div className="mb-8">
          <h2 className="text-3xl font-bold text-slate-800">
            Create New Note
          </h2>

          <p className="text-gray-500 mt-2">
            Write down ideas, reminders or important information.
          </p>
        </div>

        <div className="space-y-6">

          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-2">
              Title
            </label>

            <input
              type="text"
              placeholder="Enter note title..."
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              className="w-full rounded-lg border border-gray-300 px-4 py-3 outline-none
              focus:border-slate-700 transition-all"
            />
          </div>

          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-2">
              Description
            </label>

            <textarea
              rows="5"
              placeholder="Write your note here..."
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              className="w-full rounded-lg border border-gray-300 px-4 py-3 outline-none resize-none
              focus:border-slate-700 transition-all"
            />
          </div>

          <div className="flex justify-end">

            <button
              onClick={handleSubmit}
              className="bg-slate-800 hover:bg-slate-900 text-white
              px-8 py-3 rounded-lg font-semibold tracking-wide
              transition-all duration-200"
            >
              Create Note
            </button>

          </div>

        </div>

      </div>

    </section>
  );
};

export default NoteForm;