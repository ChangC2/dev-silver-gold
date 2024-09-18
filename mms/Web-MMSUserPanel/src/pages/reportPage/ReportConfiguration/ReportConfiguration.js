import React from 'react'

import './ReportConfiguration.css'
import OneEmailUI from './OneEmailUI/OneEmailUI';
import ReportPeriod from './ReportPeriod/ReportPeriod';
import { sizePad } from '../../../services/common/constants';
import { useSelector } from 'react-redux';
import lang from '../../../services/lang';
function ReportConfiguration(props) {
    const {langIndex} = useSelector(x=>x.app)
    const { customerInfo, selectedEmails, setSelectedEmails, screenSize } = props;
    const { fromReportDate, setFromReportDate, toReportDate, setToReportDate } = props;

    const onSelectEmail = (email, isSelect) => {
        var tmpSelectedEmails = selectedEmails;
        if (isSelect === true) {
            tmpSelectedEmails.push(email);
        } else {
            tmpSelectedEmails = tmpSelectedEmails.filter(x => x != email);
        }
        setSelectedEmails([...tmpSelectedEmails]);
    }
    if (customerInfo.emails == null){
        customerInfo.emails = "";
    }
    const EmailUI = customerInfo.emails.split(";").map((email, index) => {
        var tmpEmail = email.split(":")[0];
        if (tmpEmail == "") {
            return null;
        }

        return <OneEmailUI
            key={"emails" + index}
            email={tmpEmail}
            isSelected={selectedEmails.includes(tmpEmail)}
            onSelectEmail={onSelectEmail}
            screenSize={screenSize}
        />
    });

    return (
        screenSize.width >= sizePad
            ? // desktop version
            <div>
                <div>
                    <h3 style={{ color: "#eeeeee" }}>{lang(langIndex, "report_reportconfiguration")}</h3>
                </div>
                <div className="configuration-setting-one-item-style" >
                    <div>
                        <h4 style={{ color: "#eeeeee" }}>{lang(langIndex, "report_emails")}:</h4>
                    </div>
                    <div>
                        {EmailUI}
                    </div>
                </div>
                
            </div>
            :   // iPad Version
            <div>
                <div>
                    <h3 style={{ color: "#eeeeee" }}>{lang(langIndex, "report_reportconfiguration")}</h3>
                </div>
                <div style={{ marginLeft: 0, marginBottom: 20 }} >
                    <div>
                        <h4 style={{ color: "#eeeeee" }}>{lang(langIndex, "report_emails")}:</h4>
                    </div>
                    <div>
                        {EmailUI}
                    </div>
                </div>  
            </div>
    )
}

export default ReportConfiguration
