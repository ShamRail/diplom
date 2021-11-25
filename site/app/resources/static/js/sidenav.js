/* Set the width of the side navigation to 250px */
function openNav() {
    if(window.screen.width <= 600){
        document.getElementById("mySidenav").style.width = "100%";
    }
    else{
        document.getElementById("mySidenav").style.width = "20%";
    }
}

/* Set the width of the side navigation to 0 */
function closeNav() {
    document.getElementById("mySidenav").style.width = "0"; 
}