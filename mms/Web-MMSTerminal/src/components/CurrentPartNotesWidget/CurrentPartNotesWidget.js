import TextArea from "antd/lib/input/TextArea";
import { useState } from "react";
import "./CurrentPartNotesWidget.css";

const CurrentPartNotesWidget = (props) => {
  const { notes, setNotes } = props;
  return (
    <div className="current-part-notes-layout">
      <div className="current-part-notes-title">{"Current Part Notes"}</div>
      <TextArea
        className="current-part-notes-textarea"
        rows={3}
        max
        placeholder="Add Notes Here"
        value={notes}
        onChange={(e) => {
          setNotes(e.target.value);
        }}
      />
    </div>
  );
};

export default CurrentPartNotesWidget;
