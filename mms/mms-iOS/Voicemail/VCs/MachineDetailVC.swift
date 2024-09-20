//
//  MachineDetailVC.swift
//  Voicemail
//
//  Created by Vien Tiane on 7/15/24.
//  Copyright © 2024 newhomepage. All rights reserved.
//

import UIKit
import DGCharts
import SnapKit
import SDWebImage
import Alamofire

class MachineDetailVC: UIViewController{
    
    var machine = MMachine()
    
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var iMachine: UIImageView!
    @IBOutlet var lMachine: UILabel!
    @IBOutlet var imageWidth: NSLayoutConstraint!
    @IBOutlet var lStatus: UILabel!
    @IBOutlet var lElapsedTime: UILabel!
    @IBOutlet weak var lJobId: UILabel!
    @IBOutlet weak var lOperator: UILabel!
    @IBOutlet weak var pieChart: PieChartView!
    @IBOutlet weak var barChart: BarChartView!
    @IBOutlet weak var lineChart: LineChartView!
    
    var daysPassed = 0
    var statuses = [String]()
    var colors = [String]()
    var values = [Int]()
    var inCycleColor = ""
    var inCycleValue = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let currentDate = Date()
        let calendar = Calendar.current
        let yearStartComponents = calendar.dateComponents([.year], from: currentDate)
        guard let yearStart = calendar.date(from: yearStartComponents) else {
            fatalError("Could not create start of the year date")
        }
        daysPassed = calendar.dateComponents([.day], from: yearStart, to: currentDate).day ?? 0
        self.scrollView.hideView()
        self.apiCallForGetMachineData()
    }
    
    @IBAction func bBackTapped(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    
    func getDailyUtilization(){
        if machine.gantt.count == 0{
            statuses.append("Offline")
            colors.append("#000000")
            values.append(100)
            return
        }
        let totalTime = machine.gantt[machine.gantt.count-1].end - machine.gantt[0].start
        for item in machine.gantt{
            if !statuses.contains(item.status) || !colors.contains(item.color){
                statuses.append(item.status)
                colors.append(item.color)
                if item.status.elementsEqual("In Cycle"){
                    inCycleColor = item.color
                }
            }
        }
        
        for i in 0..<statuses.count{
            var oneValue = 0.0
            for g in machine.gantt{
                if statuses[i].elementsEqual(g.status){
                    oneValue += (Double) (g.end - g.start)
                }
            }
            oneValue = (Double(oneValue) / Double(totalTime) * 100.0)
            values.append(Int(oneValue))
            if statuses[i].elementsEqual("In Cycle"){
                inCycleValue = Int(round(oneValue))
            }
        }
        if statuses.count == 0{
            statuses.append("Offline")
            colors.append("#000000")
            values.append(100)
        }
    }
    
    func setUI(){
        self.scrollView.showView()
        var imageUrl = machine.machine_picture_url
        if !imageUrl.contains("http"){
            imageUrl = "https://slymms.com/backend/images/machine/\(imageUrl.addingPercentEncoding(withAllowedCharacters: .urlPathAllowed) ?? "blank%20machine.png")"
        }
        iMachine.sd_setImage(with: URL(string: imageUrl)) { (image, error, cache, url) in
            let width = image == nil ? 100 : self.iMachine.frame.height * image!.size.width / image!.size.height
            self.imageWidth.constant = width < self.view.frame.width ? width : self.view.frame.width
        }
        lMachine.text = machine.machine_id
        lStatus.textColor = UIColor(hex: machine.color) ?? .gray
        lStatus.text = "\(machine.status)"
        let hours = Int(machine.elapsed / 60 / 60 )
        let mins = Int((machine.elapsed - hours * 60 * 60) / 60)
        let seconds = machine.elapsed - hours * 60 * 60 - mins * 60
        lElapsedTime.text = "(Elapsed Time : \(String(format: "%02d:%02d:%02d", hours, mins, seconds)))"
        
        lJobId.text = "Job ID : \(machine.job_id)"
        lOperator.text = "Operator : \(machine.Operator)"
        
        setPieChart()
        setBarChart()
        setLineChart()
    }
    
    func setPieChart() {
        getDailyUtilization()
        pieChart.usePercentValuesEnabled = true
        pieChart.drawSlicesUnderHoleEnabled = false
        pieChart.holeRadiusPercent = 0.58
        pieChart.transparentCircleRadiusPercent = 0.61
        pieChart.chartDescription.enabled = false
        pieChart.setExtraOffsets(left: 0, top: 0, right: 0, bottom: 0)
        pieChart.drawEntryLabelsEnabled = false
        pieChart.drawCenterTextEnabled = true
        
        let paragraphStyle = NSParagraphStyle.default.mutableCopy() as! NSMutableParagraphStyle
        paragraphStyle.lineBreakMode = .byTruncatingTail
        paragraphStyle.alignment = .center
        
        
        let centerText = NSMutableAttributedString(string: "In Cycle\n\(inCycleValue)%")
        centerText.setAttributes([.font : UIFont(name: "HelveticaNeue-Light", size: 15)!,
                                  .foregroundColor : UIColor.init(hex: inCycleColor) ?? UIColor.green, .paragraphStyle : paragraphStyle], range: NSRange(location: 0, length: centerText.length))
        centerText.addAttributes([.font : UIFont(name: "HelveticaNeue-Light", size: 32)!,
                                  .foregroundColor : UIColor.init(hex: inCycleColor) ?? UIColor.green, .paragraphStyle : paragraphStyle], range: NSRange(location: 9, length: centerText.length - 9))
        if inCycleValue == 0{
            pieChart.centerAttributedText = nil
        }else{
            pieChart.centerAttributedText = centerText
        }
        
        pieChart.drawHoleEnabled = true
        pieChart.rotationAngle = 0
        pieChart.rotationEnabled = true
        pieChart.highlightPerTapEnabled = true
        
        pieChart.delegate = self
        
        let l = pieChart.legend
        l.horizontalAlignment = .center
        l.verticalAlignment = .bottom
        l.orientation = .vertical
        l.xEntrySpace = 0
        l.yEntrySpace = 0
        l.yOffset = 0
        
        var entries = [PieChartDataEntry]()
        var statusColors = [NSUIColor]()
        for i in 0..<statuses.count{
            entries.append(PieChartDataEntry(value: Double(values[i]),
                                             label: statuses[i],
                                             icon: nil))
            statusColors.append(UIColor.init(hex: colors[i]) ?? UIColor.green)
        }
        
        let set = PieChartDataSet(entries: entries, label: "")
        set.drawIconsEnabled = false
        set.sliceSpace = 1
        set.colors = statusColors
        
        let data = PieChartData(dataSet: set)
        
        let format = NumberFormatter()
        format.numberStyle = .percent
        format.maximumFractionDigits = 0
        format.multiplier = 1.0
        format.percentSymbol = "%"
        format.zeroSymbol = ""
        
        pieChart.data = data
        data.setValueFormatter(DefaultValueFormatter(formatter: format))
        data.setValueFont(.systemFont(ofSize: 14, weight: .regular))
        data.setValueTextColor(.white)
        pieChart.highlightValues(nil)
    }
    
    func setBarChart() {
        barChart.chartDescription.enabled = false
        
        barChart.dragEnabled = true
        barChart.setScaleEnabled(true)
        barChart.pinchZoomEnabled = false
        
        // ChartYAxis *leftAxis = chartView.leftAxis;
        
        barChart.rightAxis.enabled = false
        barChart.delegate = self
        
        barChart.drawBarShadowEnabled = false
        barChart.drawValueAboveBarEnabled = false
        
        barChart.maxVisibleCount = 60
        
        let xAxis = barChart.xAxis
        xAxis.labelPosition = .bottom
        xAxis.labelFont = .systemFont(ofSize: 10)
        xAxis.granularity = 1
        xAxis.labelCount = 3
        xAxis.valueFormatter = DayAxisValueFormatter(chart: barChart)
        
        let leftAxisFormatter = NumberFormatter()
        leftAxisFormatter.minimumFractionDigits = 0
        leftAxisFormatter.maximumFractionDigits = 1
        //        leftAxisFormatter.negativeSuffix = " %"
        //        leftAxisFormatter.positiveSuffix = " %"
        leftAxisFormatter.negativeSuffix = ""
        leftAxisFormatter.positiveSuffix = ""
        
        let leftAxis = barChart.leftAxis
        leftAxis.labelFont = .systemFont(ofSize: 10)
        leftAxis.labelCount = 5
        leftAxis.valueFormatter = DefaultAxisValueFormatter(formatter: leftAxisFormatter)
        leftAxis.labelPosition = .outsideChart
        leftAxis.spaceTop = 0.15
        leftAxis.axisMinimum = 0 // FIXME: HUH?? this replaces startAtZero = YES
        
        let rightAxis = barChart.rightAxis
        rightAxis.enabled = true
        rightAxis.labelFont = .systemFont(ofSize: 10)
        rightAxis.labelCount = 4
        rightAxis.valueFormatter = leftAxis.valueFormatter
        rightAxis.spaceTop = 0.15
        rightAxis.axisMinimum = 0
        
        let l = barChart.legend
        l.enabled = false
        
        
        let yVals = (1..<8).map { (i) -> BarChartDataEntry in
            let val: Double = machine.utilizations.count > i ? machine.utilizations[machine.utilizations.count - i] : 0.0
            return BarChartDataEntry(x: Double(daysPassed - 6 + i), y: val)
        }
        
        var set1: BarChartDataSet! = nil
        if let set = barChart.data?.first as? BarChartDataSet {
            set1 = set
            set1.replaceEntries(yVals)
            barChart.data?.notifyDataChanged()
            barChart.notifyDataSetChanged()
        } else {
            set1 = BarChartDataSet(entries: yVals)
            set1.colors = [NSUIColor(red: 53/255.0, green: 194/255.0, blue: 209/255.0, alpha: 1.0)]
            set1.drawValuesEnabled = false
            
            let data = BarChartData(dataSet: set1)
            data.setValueFont(UIFont(name: "HelveticaNeue-Light", size: 10)!)
            data.barWidth = 0.5
            barChart.data = data
        }
    }
    
    func setLineChart() {
        lineChart.delegate = self
        
        lineChart.chartDescription.enabled = false
        lineChart.dragEnabled = true
        lineChart.setScaleEnabled(true)
        lineChart.pinchZoomEnabled = true
        
        let xAxis = lineChart.xAxis
        xAxis.labelPosition = .bottom
        xAxis.labelCount = 6
        xAxis.valueFormatter = DateValueFormatter()
        xAxis.drawLabelsEnabled = false
        
        lineChart.legend.enabled = false
        
        let values = (0..<30).map { (i) -> ChartDataEntry in
            let val: Double = machine.utilizations.count > i ? machine.utilizations[machine.utilizations.count - i - 1] : 0.0
            return ChartDataEntry(x: Double(i), y: val)
        }
        
        let set1 = LineChartDataSet(entries: values, label: "")
        set1.drawIconsEnabled = false
        
        setupLineChartDataSet(set1)
        
        let value = ChartDataEntry(x: Double(3), y: 3)
        set1.addEntryOrdered(value)
        let gradientColors = [ChartColorTemplates.colorFromString("#000000ff").cgColor,
                              ChartColorTemplates.colorFromString("#ff0000ff").cgColor]
        let gradient = CGGradient(colorsSpace: nil, colors: gradientColors as CFArray, locations: nil)!
        
        set1.fillAlpha = 1
        set1.fill = LinearGradientFill(gradient: gradient, angle: 90)
        set1.drawFilledEnabled = true
        set1.drawValuesEnabled = false
        let data = LineChartData(dataSet: set1)
        
        lineChart.data = data
        
    }
    
    func setupLineChartDataSet(_ dataSet: LineChartDataSet) {
        if dataSet.isDrawLineWithGradientEnabled {
            dataSet.lineDashLengths = nil
            dataSet.highlightLineDashLengths = nil
            dataSet.setColors(.black, .red, .white)
            dataSet.setCircleColor(.black)
            dataSet.gradientPositions = [0, 40, 100]
            dataSet.lineWidth = 1
            dataSet.circleRadius = 3
            dataSet.drawCircleHoleEnabled = false
            dataSet.valueFont = .systemFont(ofSize: 9)
            dataSet.formLineDashLengths = nil
            dataSet.formLineWidth = 1
            dataSet.formSize = 15
        } else {
            dataSet.lineDashLengths = [5, 2.5]
            dataSet.highlightLineDashLengths = [5, 2.5]
            dataSet.setColor(.black)
            dataSet.setCircleColor(.black)
            dataSet.gradientPositions = nil
            dataSet.lineWidth = 1
            dataSet.circleRadius = 3
            dataSet.drawCircleHoleEnabled = false
            dataSet.valueFont = .systemFont(ofSize: 9)
            dataSet.formLineDashLengths = [5, 2.5]
            dataSet.formLineWidth = 1
            dataSet.formSize = 15
        }
    }
    
}

extension MachineDetailVC: ChartViewDelegate{
    // TODO: Cannot override from extensions
    //extension DemoBaseViewController: ChartViewDelegate {
    func chartValueSelected(_ chartView: ChartViewBase, entry: ChartDataEntry, highlight: Highlight) {
        NSLog("chartValueSelected");
    }
    
    func chartValueNothingSelected(_ chartView: ChartViewBase) {
        NSLog("chartValueNothingSelected");
    }
    
    func chartScaled(_ chartView: ChartViewBase, scaleX: CGFloat, scaleY: CGFloat) {
        
    }
    
    func chartTranslated(_ chartView: ChartViewBase, dX: CGFloat, dY: CGFloat) {
        
    }
}

//***************************************//
//         Mark - API Call               //
//***************************************//
extension MachineDetailVC{
    func apiCallForGetMachineData() {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "MM/dd/yyyy"
        dateFormatter.timeZone = TimeZone(secondsFromGMT: GV.factory.timezone * 3600)
        let currentDate = Date()
        let startDate = dateFormatter.string(from: currentDate)
        let tomorrowDate = Calendar.current.date(byAdding: .day, value: 1, to: currentDate) ?? currentDate
        let endDate = dateFormatter.string(from: tomorrowDate)
        
        let params: Parameters = ["customer_id": GV.factory.factory_id,
                                  "startDate": startDate,
                                  "endDate": endDate,
                                  "machine_id": "\(self.machine.machine_id)",
                                  "timezone": "\(GV.factory.timezone)"]
        GF.showLoading()
        API.postRequest(api: API.get_machine_data, params: params, completion: { result in
            GF.hideLoading()
            switch result {
            case .success(let dict):
                if dict.parseBool(param: "status"){
                    if let dataDic = dict["data"] as? NSDictionary, let mDicArray = dataDic["machines"] as? [NSDictionary], mDicArray.count > 0{
                        self.machine = MMachine.init(dict: mDicArray[0])
                        DispatchQueue.main.async {
                            self.setUI()
                        }
                    }
                }else{
                    GF.showToast(msg: dict.parseString(param: "message"))
                }            case .failure(let err_msg):
                GF.showToast(msg: err_msg)
                break
            }
        })
    }
}
