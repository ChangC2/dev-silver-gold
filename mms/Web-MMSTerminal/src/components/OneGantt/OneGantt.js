import { Spin } from "antd";
import { useState } from "react";
import { Chart } from "react-google-charts";

// import {
//   ConvertTimespanToDateBasedOnTimezone,
//   getBetweenTime,
//   GetCustomerCurrentTime,
//   GetTimeWithStyle,
// } from "../../../../services/common/cnc_functions";

// import CommentMgrDlg from "../../CommentMgrDlg/CommentMgrDlg";
import "./OneGantt.css";

function OneGantt(props) {
  // const { ganttInfo, security_level } = props;
  // const { machineInfo, customer_id } = props;
  // const { customerInfo, currentDate } = props;
  // const { myGanttList, setMyGanttList } = props;

  // const [selectedChatItem, setSelectedChatItem] = useState("");
  // const [isVisibleModal, setIsVisibleModal] = useState(false);
  // let identificationList = [];

  // let status_array = [];

  const columns = [
    { type: "string", id: "Position" },
    { type: "string", id: "President" },
    { type: "string", role: "tooltip", id: "tmp", p: { html: true } },
    { type: "datetime", id: "Start" },
    { type: "datetime", id: "End" },
  ];

  let ganttData = [
    [
      "Hyundai F650",
      "start",
      "",
      new Date("2023-05-01T17:00:00.000Z"),
      new Date("2023-05-01T17:00:00.000Z"),
    ],
    [
      "Hyundai F650",
      "Idle-Uncategorized",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Idle-Uncategorized</b></span><br><span>Time: <b>06:07:51 am - 06:08:34 am</b></span><br><span>Duration: <b>00:00:43</b></span><br><span>Operator: <b>Collin Bookout</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-01T23:07:51.000Z"),
      new Date("2023-05-01T23:08:34.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>06:08:34 am - 06:28:34 am</b></span><br><span>Duration: <b>00:20:00</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-01T23:08:34.000Z"),
      new Date("2023-05-01T23:28:34.000Z"),
    ],
    [
      "Hyundai F650",
      "Idle-Uncategorized",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Idle-Uncategorized</b></span><br><span>Time: <b>06:28:34 am - 06:31:09 am</b></span><br><span>Duration: <b>00:02:35</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-01T23:28:34.000Z"),
      new Date("2023-05-01T23:31:09.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>06:31:09 am - 06:33:40 am</b></span><br><span>Duration: <b>00:02:31</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-01T23:31:09.000Z"),
      new Date("2023-05-01T23:33:40.000Z"),
    ],
    [
      "Hyundai F650",
      "Idle-Uncategorized",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Idle-Uncategorized</b></span><br><span>Time: <b>06:33:40 am - 06:34:05 am</b></span><br><span>Duration: <b>00:00:25</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-01T23:33:40.000Z"),
      new Date("2023-05-01T23:34:05.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>06:34:05 am - 06:35:45 am</b></span><br><span>Duration: <b>00:01:40</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-01T23:34:05.000Z"),
      new Date("2023-05-01T23:35:45.000Z"),
    ],
    [
      "Hyundai F650",
      "Wait Tooling",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Wait Tooling</b></span><br><span>Time: <b>06:35:45 am - 06:42:36 am</b></span><br><span>Duration: <b>00:06:51</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-01T23:35:45.000Z"),
      new Date("2023-05-01T23:42:36.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>06:42:36 am - 06:54:15 am</b></span><br><span>Duration: <b>00:11:39</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-01T23:42:36.000Z"),
      new Date("2023-05-01T23:54:15.000Z"),
    ],
    [
      "Hyundai F650",
      "Idle-Uncategorized",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Idle-Uncategorized</b></span><br><span>Time: <b>06:54:15 am - 06:58:20 am</b></span><br><span>Duration: <b>00:04:05</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-01T23:54:15.000Z"),
      new Date("2023-05-01T23:58:20.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>06:58:20 am - 07:05:06 am</b></span><br><span>Duration: <b>00:06:46</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-01T23:58:20.000Z"),
      new Date("2023-05-02T00:05:06.000Z"),
    ],
    [
      "Hyundai F650",
      "Clear Chips",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Clear Chips</b></span><br><span>Time: <b>07:05:06 am - 07:26:42 am</b></span><br><span>Duration: <b>00:21:36</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T00:05:06.000Z"),
      new Date("2023-05-02T00:26:42.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>07:26:42 am - 07:28:42 am</b></span><br><span>Duration: <b>00:02:00</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T00:26:42.000Z"),
      new Date("2023-05-02T00:28:42.000Z"),
    ],
    [
      "Hyundai F650",
      "Idle-Uncategorized",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Idle-Uncategorized</b></span><br><span>Time: <b>07:28:42 am - 07:29:02 am</b></span><br><span>Duration: <b>00:00:20</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T00:28:42.000Z"),
      new Date("2023-05-02T00:29:02.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>07:29:02 am - 07:30:38 am</b></span><br><span>Duration: <b>00:01:36</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T00:29:02.000Z"),
      new Date("2023-05-02T00:30:38.000Z"),
    ],
    [
      "Hyundai F650",
      "Idle-Uncategorized",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Idle-Uncategorized</b></span><br><span>Time: <b>07:30:38 am - 07:33:18 am</b></span><br><span>Duration: <b>00:02:40</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T00:30:38.000Z"),
      new Date("2023-05-02T00:33:18.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>07:33:18 am - 07:44:55 am</b></span><br><span>Duration: <b>00:11:37</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T00:33:18.000Z"),
      new Date("2023-05-02T00:44:55.000Z"),
    ],
    [
      "Hyundai F650",
      "Other",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Other</b></span><br><span>Time: <b>07:44:55 am - 07:52:22 am</b></span><br><span>Duration: <b>00:07:27</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T00:44:55.000Z"),
      new Date("2023-05-02T00:52:22.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>07:52:22 am - 07:52:27 am</b></span><br><span>Duration: <b>00:00:05</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T00:52:22.000Z"),
      new Date("2023-05-02T00:52:27.000Z"),
    ],
    [
      "Hyundai F650",
      "Idle-Uncategorized",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Idle-Uncategorized</b></span><br><span>Time: <b>07:52:27 am - 07:54:27 am</b></span><br><span>Duration: <b>00:02:00</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T00:52:27.000Z"),
      new Date("2023-05-02T00:54:27.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>07:54:27 am - 08:01:09 am</b></span><br><span>Duration: <b>00:06:42</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T00:54:27.000Z"),
      new Date("2023-05-02T01:01:09.000Z"),
    ],
    [
      "Hyundai F650",
      "Clear Chips",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Clear Chips</b></span><br><span>Time: <b>08:01:09 am - 08:13:36 am</b></span><br><span>Duration: <b>00:12:27</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T01:01:09.000Z"),
      new Date("2023-05-02T01:13:36.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>08:13:36 am - 08:15:42 am</b></span><br><span>Duration: <b>00:02:06</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T01:13:36.000Z"),
      new Date("2023-05-02T01:15:42.000Z"),
    ],
    [
      "Hyundai F650",
      "Idle-Uncategorized",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Idle-Uncategorized</b></span><br><span>Time: <b>08:15:42 am - 08:15:57 am</b></span><br><span>Duration: <b>00:00:15</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T01:15:42.000Z"),
      new Date("2023-05-02T01:15:57.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>08:15:57 am - 08:17:32 am</b></span><br><span>Duration: <b>00:01:35</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T01:15:57.000Z"),
      new Date("2023-05-02T01:17:32.000Z"),
    ],
    [
      "Hyundai F650",
      "Idle-Uncategorized",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Idle-Uncategorized</b></span><br><span>Time: <b>08:17:32 am - 08:20:43 am</b></span><br><span>Duration: <b>00:03:11</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T01:17:32.000Z"),
      new Date("2023-05-02T01:20:43.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>08:20:43 am - 08:32:20 am</b></span><br><span>Duration: <b>00:11:37</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T01:20:43.000Z"),
      new Date("2023-05-02T01:32:20.000Z"),
    ],
    [
      "Hyundai F650",
      "Idle-Uncategorized",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Idle-Uncategorized</b></span><br><span>Time: <b>08:32:20 am - 08:33:20 am</b></span><br><span>Duration: <b>00:01:00</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T01:32:20.000Z"),
      new Date("2023-05-02T01:33:20.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>08:33:20 am - 08:33:25 am</b></span><br><span>Duration: <b>00:00:05</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T01:33:20.000Z"),
      new Date("2023-05-02T01:33:25.000Z"),
    ],
    [
      "Hyundai F650",
      "Idle-Uncategorized",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Idle-Uncategorized</b></span><br><span>Time: <b>08:33:25 am - 08:35:15 am</b></span><br><span>Duration: <b>00:01:50</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T01:33:25.000Z"),
      new Date("2023-05-02T01:35:15.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>08:35:15 am - 08:41:57 am</b></span><br><span>Duration: <b>00:06:42</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T01:35:15.000Z"),
      new Date("2023-05-02T01:41:57.000Z"),
    ],
    [
      "Hyundai F650",
      "Clear Chips",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Clear Chips</b></span><br><span>Time: <b>08:41:57 am - 08:54:29 am</b></span><br><span>Duration: <b>00:12:32</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T01:41:57.000Z"),
      new Date("2023-05-02T01:54:29.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>08:54:29 am - 08:54:35 am</b></span><br><span>Duration: <b>00:00:06</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T01:54:29.000Z"),
      new Date("2023-05-02T01:54:35.000Z"),
    ],
    [
      "Hyundai F650",
      "Idle-Uncategorized",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Idle-Uncategorized</b></span><br><span>Time: <b>08:54:35 am - 08:55:20 am</b></span><br><span>Duration: <b>00:00:45</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T01:54:35.000Z"),
      new Date("2023-05-02T01:55:20.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>08:55:20 am - 08:57:15 am</b></span><br><span>Duration: <b>00:01:55</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T01:55:20.000Z"),
      new Date("2023-05-02T01:57:15.000Z"),
    ],
    [
      "Hyundai F650",
      "Idle-Uncategorized",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Idle-Uncategorized</b></span><br><span>Time: <b>08:57:15 am - 08:57:45 am</b></span><br><span>Duration: <b>00:00:30</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T01:57:15.000Z"),
      new Date("2023-05-02T01:57:45.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>08:57:45 am - 08:59:25 am</b></span><br><span>Duration: <b>00:01:40</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T01:57:45.000Z"),
      new Date("2023-05-02T01:59:25.000Z"),
    ],
    [
      "Hyundai F650",
      "Idle-Uncategorized",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Idle-Uncategorized</b></span><br><span>Time: <b>08:59:25 am - 09:02:31 am</b></span><br><span>Duration: <b>00:03:06</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T01:59:25.000Z"),
      new Date("2023-05-02T02:02:31.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>09:02:31 am - 09:14:03 am</b></span><br><span>Duration: <b>00:11:32</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T02:02:31.000Z"),
      new Date("2023-05-02T02:14:03.000Z"),
    ],
    [
      "Hyundai F650",
      "Idle-Uncategorized",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Idle-Uncategorized</b></span><br><span>Time: <b>09:14:03 am - 09:16:14 am</b></span><br><span>Duration: <b>00:02:11</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T02:14:03.000Z"),
      new Date("2023-05-02T02:16:14.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>09:16:14 am - 09:22:59 am</b></span><br><span>Duration: <b>00:06:45</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T02:16:14.000Z"),
      new Date("2023-05-02T02:22:59.000Z"),
    ],
    [
      "Hyundai F650",
      "Other",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Other</b></span><br><span>Time: <b>09:22:59 am - 09:58:54 am</b></span><br><span>Duration: <b>00:35:55</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T02:22:59.000Z"),
      new Date("2023-05-02T02:58:54.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>09:58:54 am - 10:00:59 am</b></span><br><span>Duration: <b>00:02:05</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T02:58:54.000Z"),
      new Date("2023-05-02T03:00:59.000Z"),
    ],
    [
      "Hyundai F650",
      "Idle-Uncategorized",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Idle-Uncategorized</b></span><br><span>Time: <b>10:00:59 am - 10:01:09 am</b></span><br><span>Duration: <b>00:00:10</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T03:00:59.000Z"),
      new Date("2023-05-02T03:01:09.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>10:01:09 am - 10:02:44 am</b></span><br><span>Duration: <b>00:01:35</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T03:01:09.000Z"),
      new Date("2023-05-02T03:02:44.000Z"),
    ],
    [
      "Hyundai F650",
      "Clear Chips",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Clear Chips</b></span><br><span>Time: <b>10:02:44 am - 10:08:25 am</b></span><br><span>Duration: <b>00:05:41</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T03:02:44.000Z"),
      new Date("2023-05-02T03:08:25.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>10:08:25 am - 10:20:02 am</b></span><br><span>Duration: <b>00:11:37</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T03:08:25.000Z"),
      new Date("2023-05-02T03:20:02.000Z"),
    ],
    [
      "Hyundai F650",
      "Idle-Uncategorized",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Idle-Uncategorized</b></span><br><span>Time: <b>10:20:02 am - 10:22:23 am</b></span><br><span>Duration: <b>00:02:21</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T03:20:02.000Z"),
      new Date("2023-05-02T03:22:23.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>10:22:23 am - 10:29:09 am</b></span><br><span>Duration: <b>00:06:46</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T03:22:23.000Z"),
      new Date("2023-05-02T03:29:09.000Z"),
    ],
    [
      "Hyundai F650",
      "Clear Chips",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Clear Chips</b></span><br><span>Time: <b>10:29:09 am - 10:59:42 am</b></span><br><span>Duration: <b>00:30:33</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T03:29:09.000Z"),
      new Date("2023-05-02T03:59:42.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>10:59:42 am - 11:03:23 am</b></span><br><span>Duration: <b>00:03:41</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T03:59:42.000Z"),
      new Date("2023-05-02T04:03:23.000Z"),
    ],
    [
      "Hyundai F650",
      "Idle-Uncategorized",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Idle-Uncategorized</b></span><br><span>Time: <b>11:03:23 am - 11:06:33 am</b></span><br><span>Duration: <b>00:03:10</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T04:03:23.000Z"),
      new Date("2023-05-02T04:06:33.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>11:06:33 am - 11:18:10 am</b></span><br><span>Duration: <b>00:11:37</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T04:06:33.000Z"),
      new Date("2023-05-02T04:18:10.000Z"),
    ],
    [
      "Hyundai F650",
      "Clear Chips",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Clear Chips</b></span><br><span>Time: <b>11:18:10 am - 11:24:47 am</b></span><br><span>Duration: <b>00:06:37</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T04:18:10.000Z"),
      new Date("2023-05-02T04:24:47.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>11:24:47 am - 11:31:28 am</b></span><br><span>Duration: <b>00:06:41</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T04:24:47.000Z"),
      new Date("2023-05-02T04:31:28.000Z"),
    ],
    [
      "Hyundai F650",
      "Clear Chips",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Clear Chips</b></span><br><span>Time: <b>11:31:28 am - 11:42:40 am</b></span><br><span>Duration: <b>00:11:12</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T04:31:28.000Z"),
      new Date("2023-05-02T04:42:40.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>11:42:40 am - 11:44:41 am</b></span><br><span>Duration: <b>00:02:01</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T04:42:40.000Z"),
      new Date("2023-05-02T04:44:41.000Z"),
    ],
    [
      "Hyundai F650",
      "Idle-Uncategorized",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Idle-Uncategorized</b></span><br><span>Time: <b>11:44:41 am - 11:45:21 am</b></span><br><span>Duration: <b>00:00:40</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T04:44:41.000Z"),
      new Date("2023-05-02T04:45:21.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>11:45:21 am - 11:47:02 am</b></span><br><span>Duration: <b>00:01:41</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T04:45:21.000Z"),
      new Date("2023-05-02T04:47:02.000Z"),
    ],
    [
      "Hyundai F650",
      "Idle-Uncategorized",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>Idle-Uncategorized</b></span><br><span>Time: <b>11:47:02 am - 11:50:28 am</b></span><br><span>Duration: <b>00:03:26</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T04:47:02.000Z"),
      new Date("2023-05-02T04:50:28.000Z"),
    ],
    [
      "Hyundai F650",
      "In Cycle",
      '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; "><span>Status: <b>In Cycle</b></span><br><span>Time: <b>11:50:28 am - 11:50:29 am</b></span><br><span>Duration: <b>00:00:01</b></span><br><span>Operator: <b>Book, Dean</b></span><br><span>Job ID: <b>MO0000204WK</b></span><br><span>Comment: </span><div style="margin-left:20px"><b></b></div></div>',
      new Date("2023-05-02T04:50:28.000Z"),
      new Date("2023-05-02T04:50:29.000Z"),
    ],
    [
      "Hyundai F650",
      "end",
      "",
      new Date("2023-05-02T17:00:00.000Z"),
      new Date("2023-05-02T17:00:00.000Z"),
    ],
  ];

  let color_array = [
    "white",
    "#ff0000",
    "#46c392",
    "#c000db",
    "#ffec00",
    "#808080",
    "white",
  ];

   

  

  // if (ganttInfo !== undefined && ganttInfo.length > 0) {
  //   const timezone = customerInfo["timezone"];
  //   let startDate = GetCustomerCurrentTime(timezone);
  //   if (currentDate !== undefined && currentDate !== "") {
  //     startDate = new Date(currentDate);
  //   } else {
  //     startDate.setHours(0, 0, 0);
  //   }

  //   let endDate = GetCustomerCurrentTime(timezone);
  //   if (currentDate !== undefined && currentDate !== "") {
  //     endDate = new Date(currentDate);
  //   } else {
  //     endDate.setHours(0, 0, 0);
  //   }

  //   endDate.setDate(endDate.getDate() + 1);

  //   if (machineInfo["machine_id"] === "Makino") {
  //   }
  //   if (startDate.getTime() / 1000 < parseInt(ganttInfo[0].start)) {
  //     status_array.push("start");
  //     color_array.push("white");
  //     ganttData.push([
  //       machineInfo["machine_id"],
  //       "start",
  //       "",
  //       startDate,
  //       startDate,
  //     ]);
  //     identificationList.push("");
  //   }

  //   ganttData = ganttData.concat(
  //     ganttInfo.map((item) => [
  //       machineInfo["machine_id"],
  //       item["status"],
  //       createCustomHTMLContent(
  //         item["status"],
  //         ConvertTimespanToDateBasedOnTimezone(item["start"], timezone),
  //         ConvertTimespanToDateBasedOnTimezone(item["end"], timezone),
  //         item["Operator"],
  //         getBetweenTime(item["start"], item["end"]),
  //         item["job_id"] === undefined ? "" : item["job_id"],
  //         item["comment"] === null || item["comment"] === undefined
  //           ? ""
  //           : item["comment"]
  //       ),
  //       ConvertTimespanToDateBasedOnTimezone(item["start"], timezone),
  //       ConvertTimespanToDateBasedOnTimezone(item["end"], timezone),
  //     ])
  //   );
  //   identificationList = identificationList.concat(
  //     ganttInfo.map((item) => item)
  //   );

  //   for (var i = 0; i < ganttInfo.length; i++) {
  //     if (
  //       status_array.filter(
  //         (item) => item.toLowerCase() === ganttInfo[i]["status"].toLowerCase()
  //       ).length === 0
  //     ) {
  //       status_array.push(ganttInfo[i]["status"]);
  //       color_array.push(ganttInfo[i]["color"]);
  //     }
  //   }
  //   if (ganttData.length > 0) {
  //     if (endDate > ganttData[ganttData.length - 1][3]) {
  //       status_array.push("end");
  //       color_array.push("white");
  //       ganttData.push([
  //         machineInfo["machine_id"],
  //         "end",
  //         "",
  //         endDate,
  //         endDate,
  //       ]);
  //       identificationList.push("");
  //     }
  //   }
  // }

  // function createCustomHTMLContent(
  //   status,
  //   from,
  //   to,
  //   operator,
  //   duration,
  //   jobId,
  //   comment
  // ) {
  //   return (
  //     '<div style="padding:5px 5px 5px 5px; background-color:#1e1e1e; color: #eeeeee; text-align: left;width: 220px; ">' +
  //     "<span>" +
  //     lang(langIndex, "cnc_status") +
  //     ": <b>" +
  //     status +
  //     "</b></span><br>" +
  //     "<span>" +
  //     lang(langIndex, "cnc_time") +
  //     ": <b>" +
  //     GetTimeWithStyle(from) +
  //     " - " +
  //     GetTimeWithStyle(to) +
  //     "</b></span><br>" +
  //     "<span>" +
  //     lang(langIndex, "cnc_duration") +
  //     ": <b>" +
  //     duration +
  //     "</b></span><br>" +
  //     "<span>" +
  //     lang(langIndex, "plant_operator") +
  //     ": <b>" +
  //     operator +
  //     "</b></span><br>" +
  //     "<span>" +
  //     lang(langIndex, "cnc_jobid") +
  //     ": <b>" +
  //     jobId +
  //     "</b></span><br>" +
  //     "<span>" +
  //     lang(langIndex, "cnc_comment") +
  //     ': </span><div style="margin-left:20px"><b>' +
  //     comment +
  //     "</b></div>" +
  //     "</div>"
  //   );
  // }

  // const chartEvents = [
  //   {
  //     eventName: "select",
  //     callback({ chartWrapper }) {
  //       if (customer_id === undefined) return;
  //       var selectedRow = chartWrapper.getChart().getSelection()[0].row;
  //       var identification = identificationList[selectedRow];
  //       setSelectedChatItem(identification);
  //       setIsVisibleModal(true);
  //     },
  //   },
  // ];

  return (
    <div style={{ textAlign: "center", paddingTop: 20, marginRight:20 }}>
      {/* {isVisibleModal && (
        <CommentMgrDlg
          selectedChatItem={selectedChatItem}
          isVisibleModal={isVisibleModal}
          setIsVisibleModal={setIsVisibleModal}
          customer_id={customer_id}
          myGanttList={myGanttList}
          security_level={security_level}
          setMyGanttList={setMyGanttList}
          timezone={customerInfo["timezone"]}
          ganttInfo={ganttInfo}
          machineInfo={machineInfo}
        />
      )} */}

      {ganttData.length === 0 ? (
        <Spin size="medium"></Spin>
      ) : (
        <div>
          <Chart
            className="one-line-timeline"
            height={100}
            width={"100%"}
            chartType="Timeline"
            data={[columns, ...ganttData]}
            // chartEvents={chartEvents}
            options={{
              showRowNumber: false,
              showBarLabels: false,
              showName: false,
              timeline: { showRowLabels: false, showBarLabels: false },
              backgroundColor: "transparent",
              legend: "none",
              colors: color_array,
              allowHtml: true,
              tooltip: { isHtml: true },
              hAxis: {
                textStyle: {
                  color: "#FFFFFF",
                },
                gridlines: {
                  color: "#FFFFFF",
                },
                baselineColor: "#FFFFFF",
              },
            }}
          />
        </div>
      )}
    </div>
  );
}

export default OneGantt;
