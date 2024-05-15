let matrix1_area = document.querySelector("#floatingTextarea1");
let matrix2_area = document.querySelector("#floatingTextarea2");
let matrix3_area = document.querySelector("#floatingTextarea3");

let btn_calc = document.querySelector("#btn_calcutale");
let btn_clear = document.querySelector("#btn_clear_fields");

matrix1_area.value = "[]";
matrix1_area.setSelectionRange(1, 1);

matrix2_area.value = "[]";
matrix2_area.setSelectionRange(1, 1);

Array(matrix1_area, matrix2_area).forEach(el => {
    el.addEventListener("keydown", (e)=>{    // e.preventDefault();  
        if (e.code=="Space" || e.code == "Enter" || e.code=="BracketLeft" || e.code=="Comma"){
    
            let value_arr = el.value.split("");
            
            let pos = el.selectionStart;
            let value_arr_left = value_arr.slice(0, pos);
            let value_arr_right = value_arr.slice(pos);
            let substr_left = value_arr_left.join("");
            let substr_right = value_arr_right.join("").trim();
            let insert = ["    "];
            let shift = 4;
            
            if (e.code=="Enter"){
                e.preventDefault()
                if (substr_left.endsWith(']')){
                    insert = ["\n[]"];
                    shift = 2;
                }else{
                    insert = ["]\n["];
                    shift = 3;
                }
                
            }  else if (e.code=="BracketLeft"){
                insert = ["]"];
                shift =0;
            } else if (e.code=="Comma"){
                e.preventDefault();
                insert=["."];
                shift = 1;
            }
            el.value = (substr_left+insert+substr_right).trimEnd();
            el.setSelectionRange(pos+shift, pos+shift);
        }    
    });
});


btn_calc.addEventListener("click", async (e)=>{
    matrix3_area.value = "";
    e.preventDefault();
    try{
        req = await fetch('/calculate', {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
              },
            body: JSON.stringify({"value":matrix1_area.value +"|"+matrix2_area.value})
        });
        if (req.status == 200){
            try{
                const res = await req.json();
                 res.forEach((row) => {
                matrix3_area.value += `[${row.join(",  ")}]\n`;
                });
            }catch(Error) {
                alert(`Ошибка: ${Error}`);
            }           
            
        } else{
            const res = await req.text();
            alert(`Ошибка в рассчёте: ${res}`);
        }
        
    }catch(error){
        alert(`Ошибка: ${error}`);
    }
    
});

btn_clear.addEventListener("click", (e)=>{
    e.preventDefault();
    matrix1_area.value = "[]";
    matrix2_area.value ="[]";
    matrix3_area.value = "";
});
