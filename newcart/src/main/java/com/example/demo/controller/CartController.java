package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Cart;
import com.example.demo.model.User;
import com.example.demo.service.CartService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

	@Autowired
	private CartService cartService;

	@GetMapping("/cart")
	public String viewCart(HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return "redirect:/login";
		}
		List<Cart> cartItems = cartService.getCartItems(user.getId());
		double totalAmount = cartItems.stream().mapToDouble(cart -> cart.getProduct().getPrice()).sum();
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("cartCount", cartItems.size());
		model.addAttribute("totalAmount", totalAmount);
		return "cart";
	}

	@PostMapping("/addToCart")
	public String addToCart(@RequestParam Long productId, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return "redirect:/login";
		}
		cartService.addToCart(user.getId(), productId);
		return "redirect:/products";
	}

	@PostMapping("/removeFromCart")
	public String removeFromCart(@RequestParam Long productId, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return "redirect:/login";
		}
		cartService.removeFromCart(user.getId(), productId);
		return "redirect:/cart";
	}

	@GetMapping("/buyNow")
	public String buyNow(HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return "redirect:/login";
		}
		List<Cart> cartItems = cartService.getCartItems(user.getId());
		double totalAmount = cartItems.stream().mapToDouble(cart -> cart.getProduct().getPrice()).sum();

		String deliveryAddress = String.format("%s<br>%s<br>%s", user.getUsername(), user.getEmail(),
//                                               user.getPassword(),
				user.getAddress());

		String sellerAddress = cartItems.isEmpty() ? ""
				: String.format("%s<br>%s<br>%s<br>%s<br>%s<br>%s", cartItems.get(0).getProduct().getSeller().getName(),
						cartItems.get(0).getProduct().getSeller().getStreet(),
						cartItems.get(0).getProduct().getSeller().getCity(),
						cartItems.get(0).getProduct().getSeller().getState(),
						cartItems.get(0).getProduct().getSeller().getZipCode(),
						cartItems.get(0).getProduct().getSeller().getCountry());

		model.addAttribute("user", user);
		model.addAttribute("deliveryAddress", deliveryAddress);
		model.addAttribute("sellerAddress", sellerAddress);
		model.addAttribute("totalAmount", totalAmount);
		return "billing";
	}

	@PostMapping("/placeOrder")
	public String placeOrder(HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return "redirect:/login";
		}
		List<Cart> cartItems = cartService.getCartItems(user.getId());
		double totalAmount = cartItems.stream().mapToDouble(cart -> cart.getProduct().getPrice()).sum();
		cartService.placeOrder(user, totalAmount);
		model.addAttribute("message", "Order placed successfully!");
		return "orderConfirmation";
	}
}