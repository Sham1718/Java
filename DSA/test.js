const delay=(ms,value)=>new Promise((resolve)=>setTimeout(()=>resolve(value),ms));

delay(100,"hi").then(console.log);
