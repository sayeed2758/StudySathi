const state={screen:"home",setup:1,goal:"Exam Preparation",duration:15,seconds:0,timer:null,sessions:Number(localStorage.getItem("ss_sessions")||0),focus:Number(localStorage.getItem("ss_focus")||0),selectedCount:Number(localStorage.getItem("ss_selectedCount")||0)};
const screens=["welcome","setup","home","focus","protection","settings"];
const show=s=>{screens.forEach(id=>document.getElementById(id).classList.remove("active"));document.getElementById(s).classList.add("active");state.screen=s;document.querySelectorAll("nav button").forEach(b=>b.classList.toggle("active",b.dataset.screen===s));document.getElementById("nav").style.display=["home","focus","protection","settings"].includes(s)?"flex":"none";};
const setupRender=()=>{$$(".setup-step").forEach(x=>x.classList.toggle("active",Number(x.dataset.step)===state.setup));$("#setupProgress").style.width=`${state.setup*33.333}%`;$("#setupText").textContent=`${state.setup} of 3`;};
const sync=()=>{$("#homeDuration").textContent=`${state.duration} min session`;$("#homeGoal").textContent=state.goal;$("#sessionGoal").textContent=state.goal;$("#settingsDuration").textContent=`${state.duration} min`;$("#settingsGoal").textContent=state.goal;$("#selectedCount").textContent=state.selectedCount;$("#sessions").textContent=state.sessions;$("#todayFocus").textContent=`${Math.floor(state.focus/60)}m`;};
const $=s=>document.querySelector(s),$$=s=>document.querySelectorAll(s);

$("#getStarted").onclick=()=>{show("setup");state.setup=1;setupRender()};
$$(".next").forEach(b=>b.onclick=()=>{state.setup=Number(b.dataset.next);setupRender()});
$$("#distractionChoices button").forEach(b=>b.onclick=()=>b.classList.toggle("selected"));
$$("#goalChoices button").forEach(b=>b.onclick=()=>{$$("#goalChoices button").forEach(x=>x.classList.remove("selected"));b.classList.add("selected");state.goal=b.textContent.replace("○","").trim()});
$$("#durationChoices button").forEach(b=>b.onclick=()=>{$$("#durationChoices button").forEach(x=>x.classList.remove("selected"));b.classList.add("selected");state.duration=Number(b.firstChild.textContent.trim())});
$("#finishSetup").onclick=()=>{sync();show("home")};

const updateTimer=()=>{const m=Math.floor(state.seconds/60).toString().padStart(2,"0"),s=(state.seconds%60).toString().padStart(2,"0");$("#timer").textContent=`${m}:${s}`};
const startFocus=()=>{clearInterval(state.timer);state.seconds=state.duration*60;updateTimer();$("#focusHeading").textContent=state.goal;$("#timerStatus").textContent="Ready";$("#timerToggle").innerHTML='Start Focus <span>▶</span>';show("focus")};
$("#startFocus").onclick=startFocus;
$("#timerToggle").onclick=()=>{if(state.timer){clearInterval(state.timer);state.timer=null;$("#timerStatus").textContent="Paused";$("#timerToggle").innerHTML='Resume <span>▶</span>';return}$("#timerStatus").textContent="In progress";$("#timerToggle").innerHTML='Pause <span>Ⅱ</span>';state.timer=setInterval(()=>{if(state.seconds<=0){clearInterval(state.timer);state.timer=null;state.sessions++;state.focus+=state.duration*60;localStorage.setItem("ss_sessions",state.sessions);localStorage.setItem("ss_focus",state.focus);sync();$("#timerStatus").textContent="Complete";$("#timerToggle").innerHTML='Start Again <span>↻</span>';return}state.seconds--;updateTimer()},1000)};
$("#endFocus").onclick=()=>{clearInterval(state.timer);state.timer=null;show("home");sync()};
$("#focusBack").onclick=()=>{clearInterval(state.timer);state.timer=null;show("home")};
$("#openProtection").onclick=()=>{sync();show("protection")};
$("#protectionBack").onclick=()=>show("home");
$("#settingsBtn").onclick=()=>show("settings");
$("#settingsBack").onclick=()=>show("home");
$$("nav button").forEach(b=>b.onclick=()=>{if(b.dataset.screen==="focus")startFocus();else show(b.dataset.screen);sync()});
sync();show("welcome");


// StudySathi PWA installation support
if ("serviceWorker" in navigator) { window.addEventListener("load",()=>navigator.serviceWorker.register("./sw.js",{scope:"./"}).catch(console.error)); }
