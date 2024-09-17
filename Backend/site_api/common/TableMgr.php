<?php

class TableMgr
{
    function ReadColumns($tblName)
    {
        // Read columns names
        $sql = "SHOW COLUMNS FROM `$tblName`";

        include_once("connection.php");
        $db = new dbObj();
        $conn =  $db->getConnstring();

        $res = array();
        $query = mysqli_query($conn, $sql);
        if ($query) {
            while ($row = mysqli_fetch_array($query)) {
                $res[] = $row[0];
            } 
        }
        return $res;
    }

    function getConn(){
        include_once("connection.php");
        $db = new dbObj();
        $conn =  $db->getConnstring();
        return $conn;
    }

    function ReadTable($tblName, $where_text = "", $order_text = "")
    {
        ini_set('memory_limit', '-1');
        // Read columns names
        $col_names = $this->ReadColumns($tblName);
        // Read data
        $res = array(); // Result
        if ($where_text != '')
            $where_text = "where " . $where_text;
        $sql = "SELECT distinct * FROM `$tblName` " . $where_text . " " . $order_text;
        $res = $this->ExecuteQuery_With_Column($col_names, $sql);
        return $res;
    }

    function ExecuteQuery_With_Column($col_names, $sql, $timezone = null)
    {
        ini_set('memory_limit', '-1');
        // Get Db connect handle
        include_once("connection.php");
        $db = new dbObj();
        $conn =  $db->getConnstring();

        if ($timezone){
            $timezonequery = sprintf('%+d:00', $timezone);
            mysqli_query($conn, "SET time_zone='$timezonequery';");
        }

        // Read Data
        $res = array();
        $result = mysqli_query($conn, $sql);

        if ($result && mysqli_num_rows($result) > 0) {
            while ($row = mysqli_fetch_row($result)) {
                $item = array();
                for ($i = 0; $i < count($col_names); $i++) {
                    $item[$col_names[$i]] = $row[$i];
                }
                $res[] = $item;
            }
        }

        return $res;
    }

    function ExecuteQuery_Simple($sql)
    {
        // Get Db connect handle
        include_once("connection.php");
        $db = new dbObj();
        $conn =  $db->getConnstring();

        $res = array();
        $query = mysqli_query($conn, $sql);
        if ($query) {
            while ($row = mysqli_fetch_array($query)) {
                $res[] = $row;
            }
        }
        return $res;
    }

    function ExecuteQuery_Simple_NoResult($sql)
    {
        // Get Db connect handle
        include_once("connection.php");
        $db = new dbObj();
        $conn =  $db->getConnstring();

        mysqli_query($conn, $sql);

        return true;
    }

    function ExecuteQuery_Simple_Result($sql)
    {
        include_once("connection.php");
        $db = new dbObj();
        $conn =  $db->getConnstring();
        return mysqli_query($conn, $sql);
    }


    function ExecuteMultiQuery($sql)
    {
        // Get Db connect handle
        include_once("connection.php");
        $db = new dbObj();
        $conn =  $db->getConnstring();

        mysqli_multi_query($conn, $sql);

        return true;
    }

    function readDataByQuery($sql)
    {
        include_once("connection.php");
        $db = new dbObj();
        $conn =  $db->getConnstring();
        $result = mysqli_query($conn, $sql);
        $res = array();
        if ($result) {
            while ($row = mysqli_fetch_row($result)) {
                $res[] = $row;
            }
        }
        return $res;
    }

}
