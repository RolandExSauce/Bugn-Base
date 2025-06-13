const btn = $(".btn1");
const div = $(".div1");
btn.click(() => {
  div.html(
    "<img src='./loadingGif.gif' alt='' class='logo' /><span class='loader'></span></div>"
  );
  btn.text("Loading...");
  setTimeout(() => {
    btn.text("Reload");
    div.html("<p style='margin-left: 11px' >Data loaded successfully!</p>");
  }, 3000);
});
