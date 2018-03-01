<?php
$con= new mysqli("localhost","animati1_admin","Wellowwellow%5","animati1_android");
if ($con->connect_error)
{
   die('Connect Error (' . $con->connect_errno . ') '
            . $con->connect_error);
}
$emailAddress = $_POST['emailaddress'];
$password = $_POST['password'];
$res="SELECT * FROM user WHERE email='$emailAddress'";
$result2 = $con->query($res);
$row = $result2->fetch_array();
$password = $_POST['password'];
if(password_verify($password,$row['password'])){
        echo '{"query_result":"User signed in"';
        echo ',"user_result":"',$row['id'];
        echo '"}';
}
else{
       echo '{"query_result":"FAILURE"}';
   }
mysqli_close($con);
?>
