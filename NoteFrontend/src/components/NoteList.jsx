import React, { useEffect, useState } from "react";
import { getAll } from "../service/noteService";
import NoteCard from "./NoteCard";

const NoteList = ({ refresh }) => {
  const [notes, setNotes] = useState([]);

  const fetchNote = async () => {
    try {
      const response = await getAll();
      setNotes(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    fetchNote();
  }, [refresh]);

  return (
    <section className="max-w-7xl mx-auto px-6 py-8">
      <div className="mb-8">
    <h2 className="text-3xl font-bold text-slate-800">
        My Notes
    </h2>

    <p className="text-gray-500 mt-2">
        Browse and manage your saved notes.
    </p>
</div>

      {notes.length === 0 ? (
        <div className="bg-white border border-gray-200 rounded-lg py-16 text-center shadow-sm">
          <h2 className="text-2xl font-semibold text-gray-800">
            No Notes Yet
          </h2>

          <p className="mt-2 text-gray-500">
            Create your first note to get started.
          </p>
        </div>
      ) : (
        <div className="grid gap-6 sm:grid-cols-1 md:grid-cols-2 xl:grid-cols-3">
          {notes.map((note) => (
            <NoteCard
              key={note.id}
              note={note}
              onRefresh={fetchNote}
            />
          ))}
        </div>
      )}
    </section>
  );
};

export default NoteList;