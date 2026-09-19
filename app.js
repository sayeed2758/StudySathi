const $=s=>document.querySelector(s), $$=s=>document.querySelectorAll(s);
let duration=15, goal="Exam Preparation", seconds=0, timer=null, setupStep=1;
const screens={welcome:$("#welcome"),setup:$("#setup"),home:$("#home"),focus:$("#focusScreen"),settings:$("#settingsScreen")};
function show(x){Object.values(screens).forEach(s=>s.classList.remove("active"));x.classList.add("active");$("#nav").style.display=[screens.home,screens.focus,screens.settings].includes(x)?"flex":"none"}
$("#getStarted").onclick=()=>{show(screens.setup);setupStep=1;renderSetup()};
function renderSetup(){$$(".step").forEach(x=>x.classList.toggle("active",Number(x.id.replace("step",""))===setupStep));$("#pbar").style.width=(setupStep*33.33)+"%";$("#ptext").textContent=`${setupStep} of 3`;}
$$(".next").forEach(b=>b.onclick=()=>{setupStep=Number(b.dataset.step);renderSetup()});
$$("#dist button").forEach(b=>b.onclick=()=>b.classList.toggle("sel"));
$$("#goals button").forEach(b=>b.onclick=()=>{$$("#goals button").forEach(x=>x.classList.remove("sel"));b.classList.add("sel");goal=b.textContent.replace("○","").trim();});
$$("#dur button").forEach(b=>b.onclick=()=>{$$("#dur button").forEach(x=>x.classList.remove("sel"));b.classList.add("sel");duration=Number(b.firstChild.textContent.trim());});
function sync(){ $("#homeDur").textContent=`${duration} min session`;$("#homeGoal").textContent=goal;$("#goal2").textContent=goal;$("#setDur").textContent=`${duration} min`;$("#setGoal").textContent=goal;}
$("#finish").onclick=()=>{sync();show(screens.home)};
$("#focusBtn").onclick=()=>{seconds=duration*60;updateTimer();show(screens.focus)};
function updateTimer(){const m=Math.floor(seconds/60).toString().padStart(2,"0"),s=(seconds%60).toString().padStart(2,"0");$("#time").textContent=`${m}:${s}`;}
$("#timerBtn").onclick=()=>{if(timer){clearInterval(timer);timer=null;$("#timerBtn").innerHTML='Resume <span>▶</span>';$("#status").textContent="Paused";return} $("#timerBtn").innerHTML='Pause <span>Ⅱ</span>';$("#status").textContent="In progress";timer=setInterval(()=>{if(seconds<=0){clearInterval(timer);timer=null;$("#status").textContent="Complete";return}seconds--;updateTimer()},1000)};
$("#end").onclick=$("#back").onclick=()=>{clearInterval(timer);timer=null;show(screens.home)};
$("#settings").onclick=()=>show(screens.settings);$("#backSettings").onclick=()=>show(screens.home);
$$("nav button").forEach(b=>b.onclick=()=>{const t=b.dataset.to;if(t==="home")show(screens.home);if(t==="focus"){seconds=duration*60;updateTimer();show(screens.focus)}if(t==="settings")show(screens.settings);$$("nav button").forEach(x=>x.classList.remove("active"));b.classList.add("active")});
show(screens.welcome);
