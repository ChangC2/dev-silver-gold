const express = require("express");
const { networkInterfaces } = require("os");
const path = require("path");
const fs = require("fs");

const app = express();
const nets = networkInterfaces();

// Server port
const PORT = 3000;

app.get("/", (request, response) =>
  response.send("Hello from www.slymms.com!")
);

// app.get("/firmwares/standard.bin", (request, response) => {
//   response.download(
//     path.join(__dirname, "firmwares/standard.bin"),
//     "standard.bin",
//     (err) => {
//       if (err) {
//         console.error("Problem on download firmware: ", err);
//       }
//     }
//   );
// });

app.get("/firmwares/:filename", (request, response) => {
  const filename = request.params.filename;
  const filePath = path.join(__dirname, 'firmwares', filename);

  // Check if file exists
  fs.access(filePath, fs.constants.F_OK, (err) => {
    if (err) {
      console.error(`File does not exist: ${filename}`, err);
      response.status(404).send('File not found');
      return;
    }

    // File exists, download it
    response.download(filePath, filename, (downloadErr) => {
      if (downloadErr) {
        console.error(`There was an error downloading the file: ${filename}`, downloadErr);
        response.status(500).send('Error downloading file');
      }
    });
  });
});

app.listen(PORT, () => {
  const results = {}; // Or just '{}', an empty object

  for (const name of Object.keys(nets)) {
    for (const net of nets[name]) {
      // Skip over non-IPv4 and internal (i.e. 127.0.0.1) addresses
      if (net.family === "IPv4" && !net.internal) {
        if (!results[name]) {
          results[name] = [];
        }
        results[name].push(net.address);
      }
    }
  }

  console.log("Listening on port " + PORT + "\n", results);
});
