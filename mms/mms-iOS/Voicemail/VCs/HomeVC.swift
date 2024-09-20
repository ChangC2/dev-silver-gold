//
//  HomeVC.swift
//  Voicemail
//
//  Created by XiaoYan on 7/4/24.
//  Copyright © 2024 newhomepage. All rights reserved.
//

import UIKit
import SnapKit
import SDWebImage
import Alamofire

class HomeVC: BaseVC {
    
    @IBOutlet weak var iAvatar: UIImageView!
    @IBOutlet weak var lName: UILabel!
    @IBOutlet var lFactoryName: UILabel!
    @IBOutlet weak var tableView: UITableView!
    
    var topRefreshCtrl: UIRefreshControl!
    func pullToRefrensh() {
        topRefreshCtrl = UIRefreshControl()
        tableView.alwaysBounceVertical = true
        topRefreshCtrl.attributedTitle = NSAttributedString(string: "")
        topRefreshCtrl.addTarget(self, action: #selector(topRefresh(_ :)), for: UIControl.Event.valueChanged)
        if #available(iOS 10.0, *) {
            tableView.refreshControl = topRefreshCtrl
        } else {
            tableView.addSubview(topRefreshCtrl)
        }
    }
    
    @objc func topRefresh(_ sender: UIRefreshControl) {
        topRefreshCtrl.endRefreshing()
        self.apiCallForGetMachineData()
    }
    
    var allMachines = [MMachine]()
    var machines = [MMachine]()
    var groupIndex = -1
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        pullToRefrensh()
        self.tableView.register(UINib(nibName: "HomeMachineCell", bundle: nil), forCellReuseIdentifier: "HomeMachineCell")
        self.tableView.showsVerticalScrollIndicator = false
        bottomSheetTransitioningDelegate = BottomSheetTransitioningDelegate(
            contentHeights: [UIScreen.main.bounds.size.height - 50, UIScreen.main.bounds.size.height - 50],
            presentationDelegate: self
        )

        iAvatar.setBorder(color: .lightGray, width: 1)
        iAvatar.sd_setImage(with: URL(string: GV.user.user_picture), placeholderImage: UIImage(named: "ic_avatar")!)
        lName.text = "\(GV.user.username_full)"
        lFactoryName.text = "\(GV.factory.name)"
        self.apiCallForGetMachineData()
    }
    
    
    @IBAction func bFilterTapped(_ sender: UIButton) {
        if let vc = self.storyboard?.instantiateViewController(withIdentifier: "HomeFilterVC") as?  HomeFilterVC{
            vc.delegate = self
            vc.transitioningDelegate = self.bottomSheetTransitioningDelegate
            vc.modalPresentationStyle = .custom
            present(vc, animated: true)
        }
    }
    
    func setUI(){
        filterByGroup()
        self.tableView.reloadData()
    }
    
    func filterByGroup(){
        machines.removeAll()
        if groupIndex == -1{
            machines.append(contentsOf: allMachines)
        }else{
            let groupInfo = GV.factory.groupInfo[self.groupIndex]
            for machine in allMachines{
                if groupInfo.machine_list.contains(machine.machine_id){
                    self.machines.append(machine)
                }
            }
        }
    }
    
    func moveDetailVC(machine: MMachine){
        if let vc = self.storyboard?.instantiateViewController(withIdentifier: "MachineDetailVC") as? MachineDetailVC{
            vc.machine = machine
            self.navigationController?.pushViewController(vc, animated: true)
        }
    }
}

//***************************************//
//         Mark - API Call               //
//***************************************//
extension HomeVC{
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
                                  "machine_id": "",
                                  "timezone": "\(GV.factory.timezone)"]
        GF.showLoading()
        API.postRequest(api: API.get_machine_data, params: params, completion: { result in
            GF.hideLoading()
            switch result {
            case .success(let dict):
                if dict.parseBool(param: "status"){
                    if let dataDic = dict["data"] as? NSDictionary, let mDicArray = dataDic["machines"] as? [NSDictionary]{
                        self.allMachines.removeAll()
                        for mDic in mDicArray{
                            self.allMachines.append(MMachine.init(dict: mDic))
                        }
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


extension HomeVC: UITableViewDelegate,UITableViewDataSource{
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.machines.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "HomeMachineCell") as! HomeMachineCell
        cell.selectionStyle = .none
        cell.vc = self
        cell.generateCell(machine: self.machines[indexPath.row])
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        self.moveDetailVC(machine: machines[indexPath.row])
    }
    
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        
    }
}

extension HomeVC: SelectDelegate{
    func onSelect(id: String, type: Int){
        
    }
    
    func onSelect(index: Int){
        groupIndex = index
        setUI()
    }
}
