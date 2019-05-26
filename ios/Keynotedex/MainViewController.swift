//
//  ViewController.swift
//  Keynotedex
//
//  Created by Guillermo Orellana on 27/04/2019.
//  Copyright © 2019 Guillermo Orellana. All rights reserved.
//

import common
import UIKit

class MainViewController: UIViewController, ProfileView {
    
    @IBOutlet weak var label: UILabel!
    var presenter: ProfilePresenter!
 
    func setText(text:String){
        label.text = text
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let networkConfig = NetworkDataSourceKt.defaultNetworkConfig
        let sessionStorage = SessionStorage()
        let datasource = NetworkDataSource(sessionStorage: sessionStorage, networkConfig: networkConfig)
        let repository = NetworkRepository(dataSource: datasource)
        let getUserSessions = GetUserSessions(networkRepository: repository)
        presenter = PresentersKt.createProfilePresenter(view: self, getUserSessions: getUserSessions)
    }
    
    deinit {
        presenter.destroy()
    }
}
