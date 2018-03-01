<?php
error_reporting(-1); 
ini_set('dispay_errors', TRUE);
$page = $_GET['page'];

$start = 0;
//amount of videos allowed to be shown
$limit = 20;

$con = mysqli_connect("localhost","animati1_admin","Wellowwellow%5","animati1_android") or die('unable to connect to db');

$total_rows = mysqli_num_rows(mysqli_query($con, "Select image_id from videos2"));
$page_limiter = $total_rows/$limit;


    $sql = "SELECT * from videos2 limit $start, $limit";
 
    $result = mysqli_query($con,$sql); 
    $res = array(); 
    while ($row = mysqli_fetch_array($result)){
        array_push($res, array
                          ("name"=>$row['video_name'],
                           "video description"=>$row['video_description'],
                           "video"=>$row['VIDEO'],
                           "image"=>$row['videoImage'],
                           "rating"=>$row['videoRating'],
                           "rating counter"=>$row['ratingCounter']));
    }

    echo json_encode($res);
?>

