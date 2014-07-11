function delButtonPress(delButton){
    document.getElementById("deleteUser").value=delButton.id;
}
function submitToDelete(){
    document.forms["delForm"].submit();
}
function submitToDeleteMany(){
    var delList = document.getElementsByName("checkDelete");
    document.getElementById("deleteUsers").value = "";

    for (var i=0;i<delList.length;i++){
        if (delList[i].checked == true){
            var username = delList[i].id.substr(3,delList[i].id.length-1);
            username += ":::::";
            document.getElementById("deleteUsers").value += username;
        }
    }
    document.forms["delUsersForm"].submit();
}