import { useState } from "react";
import "./App.css";

import Navbar from "./components/Navbar";
import NoteForm from "./components/NoteForm";
import NoteList from "./components/NoteList";

function App() {
  const [refresh, setRefresh] = useState(false);

  return (
    <div className="min-h-screen bg-gray-100">

      <Navbar />

      <main className="pb-16">

        <NoteForm
          onCreate={() => setRefresh((prev) => !prev)}
        />

        <NoteList
          refresh={refresh}
        />

      </main>

    </div>
  );
}

export default App;