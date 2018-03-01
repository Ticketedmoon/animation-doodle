<?php
error_reporting(-1); 
ini_set('dispay_errors', TRUE);
$id = $_GET['id'];


$con = mysqli_connect("localhost","animati1_admin","Wellowwellow%5","animati1_android") or die('unable to connect to db');

$total_rows = mysqli_num_rows(mysqli_query($con, "Select image_id from videos2"));


    $sql = "SELECT * from videos2 WHERE id = $id";
 
    $result = mysqli_query($con,$sql); 
    $res = array(); 
    while ($row = mysqli_fetch_array($result)){
        array_push($res, array
                          ("name"=>$row['video_name'],
                           "video description"=>$row['video_description'],
                           "video"=>$row['VIDEO'],
                           "image"=>$row['videoImage'],
                           "rating"=>$row['videoRating']));
    }

    echo json_encode($res);
?>


