import React from "react";
import ReactDOM from "react-dom";
import "antd/dist/antd.css";
import "./NotificationIcon.css";
import "ant-design-pro/dist/ant-design-pro.css";
import NoticeIcon from "ant-design-pro/lib/NoticeIcon";
import { Tag } from "antd";
import { BellOutlined } from "@ant-design/icons";

function NotificationIcon(props) {
  const data = [
    // {
    //   id: "000000001",
    //   avatar:
    //     "",
    //   title: "New Notification",
    //   datetime: "2022-08-09",
    //   type: "notification",
    // },
  ];

  function onItemClick(item, tabProps) {}

  function onClear(tabTitle) {}

  function getNoticeData(notices) {
    if (notices.length === 0) {
      return {};
    }
    const newNotices = notices.map((notice) => {
      const newNotice = { ...notice };
      // transform id to item key
      if (newNotice.id) {
        newNotice.key = newNotice.id;
      }
      if (newNotice.extra && newNotice.status) {
        const color = {
          todo: "",
          processing: "blue",
          urgent: "red",
          doing: "gold",
        }[newNotice.status];
        newNotice.extra = (
          <Tag color={color} style={{ marginRight: 0 }}>
            {newNotice.extra}
          </Tag>
        );
      }
      return newNotice;
    });
    return newNotices.reduce((pre, data) => {
      if (!pre[data.type]) {
        pre[data.type] = [];
      }
      pre[data.type].push(data);
      return pre;
    }, {});
  }

  const noticeData = getNoticeData(data);

  return (
    <NoticeIcon
      className="notice-icon"
      count={5}
      onItemClick={onItemClick}
      onClear={onClear}
      bell={<BellOutlined className="bell-icon" />}
    >
      <NoticeIcon.Tab
        list={noticeData.notification}
        title="Notifications"
        emptyText="Empty"
        emptyImage="https://gw.alipayobjects.com/zos/rmsportal/wAhyIChODzsoKIOBHcBk.svg"
      />
    </NoticeIcon>
  );
}

export default NotificationIcon;
