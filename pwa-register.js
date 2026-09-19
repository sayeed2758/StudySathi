if ("serviceWorker" in navigator) {
  window.addEventListener("load", () => {
    navigator.serviceWorker.register("./sw.js", {scope:"./"})
      .then(() => console.log("StudySathi PWA ready"))
      .catch(err => console.error("StudySathi PWA registration failed:", err));
  });
}
