function delButtonPress(delButton){
    document.getElementById("deleteRow").value=delButton.id;
}
function submitToDelete(){
    document.forms["delForm"].submit();
}
function submitToDeleteMany(){
    var delList = document.getElementsByName("checkDelete");
    document.getElementById("deleteRows").value = "";

    for (var i=0;i<delList.length;i++){
        if (delList[i].checked == true){
            var rowID = delList[i].id.substr(3,delList[i].id.length-1);
            rowID += ":::::";
            document.getElementById("deleteRows").value += rowID;
        }
    }
    document.forms["delManyForm"].submit();
}