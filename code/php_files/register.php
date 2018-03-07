<?php
$con= new mysqli("localhost","animati1_admin","Wellowwellow%5","animati1_android");
if ($con->connect_error)
{
   die('Connect Error (' . $con->connect_errno . ') '
            . $con->connect_error);
}
$emailAddress = $_POST['emailaddress'];
$passWord = $_POST['password'];
$encryptedPassword = password_hash($_POST['password'], PASSWORD_DEFAULT);
$result = "INSERT INTO user (email, password) 
          VALUES ('$emailAddress','$encryptedPassword')";



$sql = $con->query($result);

$res="SELECT * FROM user WHERE email='$emailAddress'";
$sql2 = mysqli_query($con,$res);
$row = $sql2->fetch_array();
if($sql == true) {
    $rowid = $row['id'];
    $result2 = "INSERT INTO profile (id) 
          VALUES ('$rowid')";
          $sql3 = $con->query($result2);
       echo '{"query_result":"SUCCESS"';
       echo ',"user_result":"',$row['id'];
        echo '"}';
   }
else{
       echo '{"query_result":"FAILURE"}';
   }
mysqli_close($con);
?>
