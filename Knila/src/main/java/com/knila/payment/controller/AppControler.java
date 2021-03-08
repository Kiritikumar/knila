package com.knila.payment.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

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

	/**
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/register")
	public String showSignUpForm(Model model) {
		model.addAttribute("user", new User());
		return "signup_form";
	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping("/process_register")
	public String processRegistration(User user) {
		String returnPage = "register_success";
		try {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String encodedPassword = encoder.encode(user.getPassword());
			user.setPassword(encodedPassword);
			userRepo.save(user);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			returnPage = "errorPage";
		}
		return returnPage;
	}

	/**
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/users")
	public String listUsers(Model model) {
		String returnPage = "users";
		try {
			List<User> listUsers = userRepo.findAll();
			model.addAttribute("listUsers", listUsers);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			returnPage = "errorPage";
		} catch (Exception e) {
			e.printStackTrace();
			returnPage = "errorPage";
		}
		return returnPage;
	}

	/**
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/listPayers")
	public String listPayers(Model model) {
		String returnPage = "searchResult";
		try {
			List<Payer> listPayer = payerRepo.findAll();
			model.addAttribute("listPayer", listPayer);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			returnPage = "errorPage";
		} catch (Exception e) {
			e.printStackTrace();
			returnPage = "errorPage";
		}
		return returnPage;
	}

	/**
	 * 
	 * @param accountNumber
	 * @param payerName
	 * @param address
	 * @param model
	 * @return
	 */
	@RequestMapping("/search")
	public String search(@RequestParam(value = "accountNumber", required = false) String accountNumber,
			@RequestParam(value = "payerName", required = false) String payerName,
			@RequestParam(value = "address", required = false) String address, Model model) {
		String returnPage = "searchResult";
		List<Payer> listedPayers = new ArrayList<Payer>();

		try {
			if (StringUtils.isEmpty(accountNumber) && StringUtils.isEmpty(payerName) && StringUtils.isEmpty(address)) {
				listedPayers = payerRepo.findAll();
			} else {
				listedPayers = payerRepo.findDistinctPayerByAccountNumberOrPayerNameOrAddress(accountNumber, payerName,
						address);
			}

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			returnPage = "errorPage";
		} catch (Exception e) {
			e.printStackTrace();
			returnPage = "errorPage";
		}

		model.addAttribute("listPayer", listedPayers);
		return returnPage;
	}

	/**
	 * 
	 * @param model
	 * @param cartItems
	 * @return
	 */
	@GetMapping("/cart")
	public String cart(Model model, @RequestParam("cartItems") String cartItems) {

		try {
			List<Payer> listPayer = new ArrayList<>();
			if (!StringUtils.isEmptyOrWhitespace(cartItems)) {
				List<Long> longCartIds = Arrays.asList(cartItems.split(",")).stream().map(String::trim)
						.map(a -> Long.parseLong(a)).collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(longCartIds)) {
					listPayer = payerRepo.findByPkPayerIdIn(longCartIds);
				}
			}

			model.addAttribute("listPayer", listPayer);
			model.addAttribute("totalCartAmount",
					listPayer.stream().mapToLong(a -> Long.parseLong(a.getAmount())).sum());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "cart";
	}

	/**
	 * 
	 * @param cartItems
	 * @return
	 */
	private List<Long> convertStrArrToLongList(String cartItems) {
		List<Long> cartIds = null;
		if (!StringUtils.isEmptyOrWhitespace(cartItems)) {
			List<String> pkIds = Arrays.asList(cartItems.split(","));
			if (!CollectionUtils.isEmpty(pkIds)) {
				for (String id : pkIds) {
					if (!StringUtils.isEmptyOrWhitespace(id) && id.matches("[0-9]")) {
						if (null == cartIds) {
							cartIds = new ArrayList<Long>();
						}
						cartIds.add(Long.getLong(id.trim()));
					}

				}
			}
		}

		/*
		 * return Arrays.asList(cartItems.split(",")) .stream() .map(String::trim)
		 * .map(a -> Long.parseLong(a)) .collect(Collectors.toList());
		 */
		return cartIds;
	}

}
