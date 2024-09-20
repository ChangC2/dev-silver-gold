<?php

namespace App\Controllers\api;

use App\Controllers\BaseController;
use App\Models\UserModel;
use App\Models\UserSubscriptionModel;

class User extends BaseController
{

    private UserModel $userModel;
    private UserSubscriptionModel $userSubscriptionModel;

    public function __construct()
    {
        $this->userModel = new UserModel();
        $this->userSubscriptionModel = new UserSubscriptionModel();
    }

    public function index()
    {
    }

    public function login()
    {
        ini_set('session.gc_maxlifetime', 3600 * 24 * 365);
        session_set_cookie_params(3600 * 24 * 365);
        session_start();

        $email = $_POST['email'];
        $password = $_POST['password'];

        $user = $this->userModel->where(array("email" => $email))->findAll();
        if (count($user) > 0) {
            foreach ($user as $item) {
                $hash_pass = $item['password'];
                if (password_verify($password, $hash_pass)) {
                    if ($item["active"] == 0) {
                        $verification_code = rand(1000, 9999);
                        $data = array(
                            "verification_code" => $verification_code,
                        );
                        $this->userModel->update($item["id"], $data);
                        $mailBody = "Verification code : $verification_code";
                        $this->send_smtp(trim($email), SITE_TITLE, $mailBody);
                    } else {
                        $_SESSION['user_id'] = $item['id'];
                        $_SESSION['email'] = $email;
                        $_SESSION['role'] = $item['role'];
                    }
                    $data = array(
                        "res" => "success",
                        "user" => $item
                    );
                    die(json_encode($data));
                }
            }
        }
        $data = array(
            "res" => "fail"
        );
        die(json_encode($data));
    }

    public function register()
    {
        ini_set('session.gc_maxlifetime', 3600 * 24 * 365);
        session_set_cookie_params(3600 * 24 * 365);
        session_start();

        $name = $_POST['name'];
        $first_name = $_POST['first_name'];
        $last_name = $_POST['last_name'];
        $email = $_POST['email'];
        $password = $_POST['password'];
        $role = "0";

        $user = $this->userModel->where(array("email" => $email))->findAll();
        if (count($user) > 0) {
            $data = array(
                "res" => "exist"
            );
        } else {
            $verification_code = rand(1000, 9999);
            $data = array(
                "name" => $name,
                "first_name" => $first_name,
                "last_name" => $last_name,
                "email" => $email,
                "password" => password_hash($password, PASSWORD_BCRYPT),
                "verification_code" => $verification_code,
                "active" => 0,
                "role" => $role,
            );
            $this->userModel->insert($data);
            $mailBody = "Verification code : $verification_code";
            $this->send_smtp(trim($email), SITE_TITLE, $mailBody);
            $data = array(
                "res" => "success",
                "email" => $email
            );
        }
        die(json_encode($data));
    }

    public function resetpw()
    {
        ini_set('session.gc_maxlifetime', 3600 * 24 * 365);
        session_set_cookie_params(3600 * 24 * 365);
        session_start();

        $email = $_POST['email'];
        $user = $this->userModel->where(array("email" => $email))->findAll();
        if (count($user) > 0) {
            $verification_code = rand(1000, 9999);
            $data = array(
                "verification_code" => $verification_code,
            );
            $this->userModel->update($user[0]["id"], $data);
            $mailBody = "Verification code : $verification_code";
            $this->send_smtp(trim($email), SITE_TITLE, $mailBody);
            $data = array(
                "res" => "success",
                "email" => $email
            );
        } else {
            $data = array(
                "res" => "fail",
                "message" => "no user"
            );
        }
        die(json_encode($data));
    }

    public function verify()
    {
        ini_set('session.gc_maxlifetime', 3600 * 24 * 365);
        session_set_cookie_params(3600 * 24 * 365);
        session_start();

        $email = $_POST['email'];
        $verification_code = $_POST['verification_code'];
        $user = $this->userModel->where(array("email" => $email, "verification_code" => $verification_code))->findAll();
        if (count($user) > 0) {
            $data = array(
                "verification_code" => "",
                "active" => 1,
            );
            $this->userModel->update($user[0]["id"], $data);
            $_SESSION['user_id'] = $user[0]["id"];
            $_SESSION['email'] = $email;
            $_SESSION['role'] = $user[0]["role"];
            $data = array(
                "res" => "success",
                "user" => $user[0]
            );
            die(json_encode($data));
        } else {
            $data = array(
                "res" => "fail",
            );
        }
        die(json_encode($data));
    }

    public function resend_code()
    {
        ini_set('session.gc_maxlifetime', 3600 * 24 * 365);
        session_set_cookie_params(3600 * 24 * 365);
        session_start();

        $email = $_POST['email'];
        $user = $this->userModel->where(array("email" => $email))->findAll();
        if (count($user) > 0) {
            $mailBody = "Verification code : " . $user[0]["verification_code"];
            $this->send_smtp(trim($email), SITE_TITLE, $mailBody);
        }
        $data = array(
            "res" => "success",
        );
        die(json_encode($data));
    }

    public function update_profile()
    {
        $request_json = file_get_contents('php://input');
        $params = json_decode($request_json, true);
        $id = $params['id'];
        $mobile_number = $params['mobile_number'];
        $first_name = $params['first_name'];
        $last_name = $params['last_name'];
        $name = $params['name'];


        $data = array(
            "first_name" => $first_name,
            "last_name" => $last_name,
            "name" => $name,
            "mobile_number" => $mobile_number,
        );

        if (isset($params['password']) != null) {
            $password = password_hash($params['password'], PASSWORD_BCRYPT);
            $data["password"] = $password;
        }

        $this->userModel->update($id, $data);
        $res = array(
            "res" => "success"
        );
        echo json_encode($res);
    }

    public function update_address()
    {
        $request_json = file_get_contents('php://input');
        $params = json_decode($request_json, true);

        $id = $params['id'];
        $address = $params['address'];
        $city = $params['city'];
        $street = $params['street'];
        $state = $params['state'];
        $zip = $params['zip'];

        $data = array(
            "address" => $address,
            "city" => $city,
            "street" => $street,
            "state" => $state,
            "zip" => $zip,
        );
        $this->userModel->update($id, $data);
        $res = array(
            "res" => "success"
        );
        echo json_encode($res);
    }

    public function update_bio()
    {
        $request_json = file_get_contents('php://input');
        $params = json_decode($request_json, true);

        $id = $params['id'];
        $bio = $params['bio'];

        $data = array(
            "bio" => $bio,
        );
        $this->userModel->update($id, $data);
        $res = array(
            "res" => "success"
        );
        echo json_encode($res);
    }

    public function delete()
    {
        $request_json = file_get_contents('php://input');
        $params = json_decode($request_json, true);
        $user_id = $params['id'];
        if ($this->userModel->find($user_id)["role"] == 1) {
            $data = array(
                "res" => "fail"
            );
            die(json_encode($data));
        }
        $this->userModel->delete($user_id);
        $data = array(
            "res" => "success"
        );
        die(json_encode($data));
    }

    public function payment_init()
    {
        ini_set('session.gc_maxlifetime', 3600 * 24 * 365);
        session_set_cookie_params(3600 * 24 * 365);
        session_start();

        // Include the configuration file 
        // require_once 'vendor/autoload.php';


        // Set API key 
        \Stripe\Stripe::setApiKey(STRIPE_API_KEY);
        // Retrieve JSON from POST body 
        $jsonStr = file_get_contents('php://input');
        $jsonObj = json_decode($jsonStr, true);
        // Get user ID from current SESSION 
        $user_id = $_SESSION['user_id'];
        $user = $this->userModel->find($user_id);

        if ($jsonObj["request_type"] == 'create_customer_subscription') {
            $subscr_plan_id = !empty($jsonObj["subscr_plan_id"]) ? $jsonObj["subscr_plan_id"] : '';
            $planName = "Monthly Subscription";
            $planPrice = 5;
            $planInterval = "month";
            $intervalCount = 1;
            // Convert price to cents 
            $planPriceCents = round($planPrice * 100);
            // Add customer to stripe 
            try {
                $customer = \Stripe\Customer::create([
                    'name' => $user["name"],
                    'email' => $user["email"]
                ]);
            } catch (\Stripe\Exception\ApiErrorException $e) {
                $api_error = $e->getMessage();
            }

            if (empty($api_error) && $customer) {
                try {
                    // Create price with subscription info and interval 
                    $price = \Stripe\Price::create([
                        'unit_amount' => $planPriceCents,
                        'currency' => STRIPE_CURRENCY,
                        'recurring' => ['interval' => $planInterval, 'interval_count' => $intervalCount],
                        'product_data' => ['name' => $planName],
                    ]);
                } catch (\Stripe\Exception\ApiErrorException $e) {
                    $api_error = $e->getMessage();
                }

                if (empty($api_error) && $price) {
                    // Create a new subscription 
                    try {
                        $subscription = \Stripe\Subscription::create([
                            'customer' => $customer->id,
                            'items' => [[
                                'price' => $price->id,
                            ]],
                            'payment_behavior' => 'default_incomplete',
                            'payment_settings' => ['save_default_payment_method' => 'on_subscription'],
                            'expand' => ['latest_invoice.payment_intent'],
                        ]);
                    } catch (\Stripe\Exception\ApiErrorException $e) {
                        $api_error = $e->getMessage();
                    }


                    if (empty($api_error) && $subscription) {
                        $output = [
                            'subscriptionId' => $subscription->id,
                            'clientSecret' => $subscription->latest_invoice->payment_intent["client_secret"],
                            'customerName' => $user["name"],
                            'customerId' => $customer->id
                        ];
                        echo json_encode($output);
                    } else {
                        echo json_encode(['error' => $api_error]);
                    }
                } else {
                    echo json_encode(['error' => $api_error]);
                }
            } else {
                echo json_encode(['error' => $api_error]);
            }
        } elseif ($jsonObj["request_type"] == 'payment_insert') {
            $payment_intent = !empty($jsonObj["payment_intent"]) ? $jsonObj["payment_intent"] : '';
            $subscription_id = !empty($jsonObj["subscription_id"]) ? $jsonObj["subscription_id"] : '';
            $customer_id = !empty($jsonObj["customer_id"]) ? $jsonObj["customer_id"] : '';
            $subscr_plan_id = !empty($jsonObj["subscr_plan_id"]) ? $jsonObj["subscr_plan_id"] : '';

            $card_name = !empty($jsonObj["card_name"]) ? $jsonObj["card_name"] : '';
            $address_cc = !empty($jsonObj["address_cc"]) ? $jsonObj["address_cc"] : '';
            $street = !empty($jsonObj["street"]) ? $jsonObj["street"] : '';
            $city = !empty($jsonObj["city"]) ? $jsonObj["city"] : '';
            $state = !empty($jsonObj["state"]) ? $jsonObj["state"] : '';
            $zip = !empty($jsonObj["zip"]) ? $jsonObj["zip"] : '';

            // Retrieve customer info 
            try {
                $customer = \Stripe\Customer::retrieve($customer_id);
            } catch (\Stripe\Exception\ApiErrorException $e) {
                $api_error = $e->getMessage();
            }

            // Check whether the charge was successful 
            if (!empty($payment_intent) && $payment_intent["status"] == 'succeeded') {
                $payment_intent_id = $payment_intent["id"];
                $paidAmount = $payment_intent["amount"];
                $paidAmount = ($paidAmount / 100);
                $paidCurrency = $payment_intent["currency"];
                $payment_status = $payment_intent["status"];
                $created = date("Y-m-d H:i:s", $payment_intent["created"]);
                // Retrieve subscription info 
                try {
                    $subscriptionData = \Stripe\Subscription::retrieve($subscription_id);
                } catch (\Stripe\Exception\ApiErrorException $e) {
                    $api_error = $e->getMessage();
                }

                $default_payment_method = $subscriptionData->default_payment_method;
                $default_source = $subscriptionData->default_source;
                $plan_obj = $subscriptionData->plan;
                $plan_price_id = $plan_obj->id;
                $plan_interval = $plan_obj->interval;
                $plan_interval_count = $plan_obj->interval_count;

                $current_period_start = $current_period_end = '';
                if (!empty($subscriptionData)) {
                    $created = date("Y-m-d H:i:s", $subscriptionData->created);
                    $current_period_start = date("Y-m-d H:i:s", $subscriptionData->current_period_start);
                    $current_period_end = date("Y-m-d H:i:s", $subscriptionData->current_period_end);
                }

                $customer_name = $customer_email = '';
                if (!empty($customer)) {
                    $customer_name = !empty($customer->name) ? $customer->name : '';
                    $customer_email = !empty($customer->email) ? $customer->email : '';
                }
                // Check if any transaction data exists already with the same TXN ID 
                $userSubscription = $this->userSubscriptionModel->where(array("stripe_payment_intent_id" => $payment_intent_id))->first();
                $payment_id = 0;

                $_SESSION['role'] = 1;
                $this->userModel->update($user_id, array("role" => 1));

                if (isset($userSubscription) && !empty($userSubscription["id"])) {
                    $payment_id = $userSubscription["id"];
                } else {
                    // Insert transaction data into the database 
                    $data = array(
                        "user_id" => $user_id,
                        "plan_id" => $subscr_plan_id,
                        "stripe_customer_id" => $customer_id,
                        "stripe_plan_price_id" => $plan_price_id,
                        "stripe_payment_intent_id" => $payment_intent_id,
                        "stripe_subscription_id" => $subscription_id,
                        "default_payment_method" => $default_payment_method,
                        "default_source" => $default_source,
                        "paid_amount" => $paidAmount,
                        "paid_amount_currency" => $paidCurrency,
                        "plan_interval" => $plan_interval,
                        "plan_interval_count" => $plan_interval_count,
                        "customer_name" => $customer_name,
                        "customer_email" => $customer_email,
                        "plan_period_start" => $current_period_start,
                        "plan_period_end" => $current_period_end,
                        "created" => $created,
                        "status" => $payment_status,
                        "card_name" => $card_name,
                        "address_cc" => $address_cc,
                        "street" => $street,
                        "city" => $city,
                        "state" => $state,
                        "zip" => $zip,

                    );
                    $payment_id = $this->userSubscriptionModel->insert($data);
                }
                $output = [
                    'payment_id' => base64_encode($payment_id)
                ];
                echo json_encode($output);
            } else {
                echo json_encode(['error' => 'Transaction has been failed!']);
            }
        }
    }

    public function cancel_subscription()
    {
        ini_set('session.gc_maxlifetime', 3600 * 24 * 365);
        session_set_cookie_params(3600 * 24 * 365);
        session_start();

        \Stripe\Stripe::setApiKey(STRIPE_API_KEY);
        $user_id = $_SESSION['user_id'];
        $userSubscription = $this->userSubscriptionModel->where(array("user_id" => $user_id))->first();
        if (isset($userSubscription) && !empty($userSubscription["id"])) {
            $stripe_subscription_id = $userSubscription["stripe_subscription_id"];
            $schedule = \Stripe\Subscription::retrieve($stripe_subscription_id);
            $schedule->cancel();
            $this->userSubscriptionModel->where(array("user_id" => $user_id))->delete();
            $_SESSION['role'] = 0;
            $this->userModel->update($user_id, array("role" => 0));
        }
        $res = array(
            "res" => "success"
        );
        echo json_encode($res);
    }
}
