import React, { useState } from "react";
import { deleteNote, updateNote } from "../service/noteService";

const NoteCard = ({ note, onRefresh }) => {
  const [isEditing, setIsEditing] = useState(false);
  const [title, setTitle] = useState(note.title);
  const [description, setDescription] = useState(note.description);

  const handleDelete = async (id) => {
    try {
      await deleteNote(id);
      onRefresh?.();
      alert("Note deleted successfully");
    } catch (error) {
      console.error(error);
    }
  };

  const handleUpdate = async () => {
    try {
      const updatedNote = {
        title,
        description,
      };

      await updateNote(note.id, updatedNote);

      alert("Note updated successfully");

      setIsEditing(false);

      onRefresh?.();
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className="bg-white border border-gray-200 rounded-xl shadow-sm hover:shadow-lg transition-all duration-300 p-6 h-full flex flex-col justify-between">

      {isEditing ? (
        <div className="space-y-4">

          <h2 className="text-xl font-semibold text-slate-800">
            Edit Note
          </h2>

          <input
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            placeholder="Title"
            className="w-full border border-gray-300 rounded-lg px-4 py-2.5 outline-none focus:border-slate-700 transition"
          />

          <textarea
            rows="4"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Description"
            className="w-full border border-gray-300 rounded-lg px-4 py-2.5 outline-none resize-none focus:border-slate-700 transition"
          />

          <div className="flex justify-end gap-3 pt-2">

            <button
              onClick={() => setIsEditing(false)}
              className="px-5 py-2 rounded-lg border border-gray-300 text-gray-700 hover:bg-gray-100 transition"
            >
              Cancel
            </button>

            <button
              onClick={handleUpdate}
              className="px-5 py-2 rounded-lg bg-slate-800 text-white hover:bg-slate-900 transition"
            >
              Save
            </button>

          </div>

        </div>
      ) : (
        <>
          <div>

            <div className="flex items-start justify-between mb-4">

              <h2 className="text-2xl font-bold text-slate-800 wrap-break-word">
                {note.title}
              </h2>

              <span className="text-xs font-medium bg-amber-100 text-amber-700 px-3 py-1 rounded-full">
                NOTE
              </span>

            </div>

            <p className="text-gray-600 leading-7 whitespace-pre-wrap">
              {note.description}
            </p>

          </div>

          <div className="flex justify-end gap-3 mt-8">

            <button
              onClick={() => setIsEditing(true)}
              className="px-4 py-2 rounded-lg bg-amber-600 text-white hover:bg-amber-700 transition font-medium"
            >
              Edit
            </button>

            <button
              onClick={() => handleDelete(note.id)}
              className="px-4 py-2 rounded-lg bg-red-600 text-white hover:bg-red-700 transition font-medium"
            >
              Delete
            </button>

          </div>
        </>
      )}
    </div>
  );
};

export default NoteCard;