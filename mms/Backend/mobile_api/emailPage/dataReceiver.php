<?php
class dataReceiver
{
	function ReadData()
	{
		include_once("tableReader.php");
		$reader = new tableReader();

		include_once("oneUser.php");
		$idList = $reader->readInfoTables();
		$info = array();
		$kk = 0;
		for ($i = 0; $i < count($idList); $i++) {
			$user = new oneUser();
			$tmp = array();
			$tmp = $user->ReadData($idList[$i]);
			if ($tmp != null) {
				$info[] = $tmp;
				$kk++;
			}
			unset($user);
		}
		return $info;
	}
}
