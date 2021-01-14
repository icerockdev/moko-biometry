/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import UIKit
import MultiPlatformLibrary

class TestViewController: UIViewController {
    
    private var viewModel: SampleViewModel!
        
    override func viewDidLoad() {
        super.viewDidLoad()

        self.viewModel = SampleViewModel(eventsDispatcher: Mvvm_coreEventsDispatcher(listener: self))
        
    }
    
    @IBAction private func loginAction() {
        self.viewModel.tryToAuth()
    }
}

extension TestViewController: SampleViewModelEventListener {
    func onFail() {
        let alertAction = UIAlertAction(title: "Ok", style: .default, handler: nil)
        let alert = UIAlertController(title: "Opps", message: "Something went wrong", preferredStyle: .alert)
        alert.addAction(alertAction)
        self.present(alert, animated: true, completion: nil)
    }
    
    func onSuccess() {
        let alertAction = UIAlertAction(title: "Ok", style: .default, handler: nil)
        let alert = UIAlertController(title: "Success", message: "You have successfully logged in", preferredStyle: .alert)
        alert.addAction(alertAction)
        self.present(alert, animated: true, completion: nil)
    }
}
