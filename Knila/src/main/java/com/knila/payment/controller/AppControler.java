package com.knila.payment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.knila.payment.dao.PayerRepository;
import com.knila.payment.dao.UserRepository;
import com.knila.payment.model.Payer;
import com.knila.payment.model.User;

@Controller
public class AppControler {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PayerRepository payerRepo;

	@GetMapping("/")
	public String viewHomePage() {
		return "index";
	}

	@GetMapping("/register")
	public String showSignUpForm(Model model) {
		model.addAttribute("user", new User());
		return "signup_form";
	}

	@PostMapping("/process_register")
	public String processRegistration(User user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		userRepo.save(user);
		return "register_success";
	}

	@GetMapping("/users")
	public String listUsers(Model model) {
		List<User> listUsers = userRepo.findAll();
		model.addAttribute("listUsers", listUsers);
		return "users";
	}

	@GetMapping("/listPayers")
	public String listPayers(Model model) {
		try {
			List<Payer> listPayer = payerRepo.findAll();
			model.addAttribute("listPayer", listPayer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "searchResult";
	}

	@RequestMapping("/search")
	public String search(@RequestParam(value = "accountNumber", required = false) String accountNumber,
			@RequestParam(value = "payerName", required = false) String payerName,
			@RequestParam(value = "address", required = false) String address, Model model) {
		List<Payer> listedPayers = null;

		try {
			if (StringUtils.isEmpty(accountNumber) && StringUtils.isEmpty(payerName) && StringUtils.isEmpty(address)) {
				listedPayers = payerRepo.findAll();
			} else {
				listedPayers = payerRepo.findDistinctPayerByAccountNumberOrPayerNameOrAddress(accountNumber, payerName,
						address);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("listPayer", listedPayers);

		return "searchResult";
	}

}
