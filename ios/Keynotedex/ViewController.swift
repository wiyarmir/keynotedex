//
//  ViewController.swift
//  Keynotedex
//
//  Created by Guillermo Orellana on 27/04/2019.
//  Copyright © 2019 Guillermo Orellana. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        let label = UILabel(frame: CGRect(x: 0, y: 0, width: 300, height: 21))
        label.center = CGPoint(x: 160, y: 285)
        label.textAlignment = .center
        label.font = label.font.withSize(25)
        label.text = "Hello"
        view.addSubview(label)
    }
    
}

