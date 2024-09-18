// @flow strict

import { Row, Col, message, Select, Spin } from "antd";
import NormalButton from "components/ButtonWidgets/NormalButton/NormalButton";
import React, { useState } from "react";
import AsyncSelect from "react-select/async";
import { apiSearchProduct } from "services/productService";
import "./InputSearchProduct.css";
const { Option } = Select;
function InputSearchProduct(props) {
  const { onSelectProduct } = props;
  const [searchedProducts, setSearchedProducts] = useState([]);
  // const [selectedProduct, setSelectedProduct] = useState([]);
  const onChangeKeyword = (keyword) => {
    if (keyword === "" || keyword === undefined) {
      setSearchedProducts([]);
    } else {
      apiSearchProduct(keyword)
        .then((res) => {
          setSearchedProducts(
            res.map((product) => ({
              ...product,
              key: product.barcode,
            }))
          );
        })
        .catch((err) => {
          setSearchedProducts([]);
          message.error(err);
        });
    }
  };
  const selectProduct = (selectedProduct) => {
    if (
      selectedProduct === undefined ||
      Object.keys(selectedProduct).length === 0 || 
      selectedProduct.value === undefined
    ) {
      message.warn("Please select the product you want to search.");
      return;
    }
    const barcode = selectedProduct.value;
    const product = searchedProducts.find((x) => x.barcode === barcode);
    if (product === undefined) {
      message.warn("Please select the product you want to search.");
      return;
    }
    onSelectProduct(product);
  };

  return (
    <Row align={"middle"} justify={"center"}>
      <Col style={{ width: "80%" }}>
        <div className="input-search-product-widget">
          <Select
            size={"medium"}
            labelInValue
            placeholder="Search product by keyword."
            filterOption={false}
            showSearch={true}
            onSearch={onChangeKeyword}
            style={{ width: "100%" }}
            value={null}
            onSelect={(v, a, c) => {
              selectProduct(v);
              // setSelectedProduct(v);
            }}
          >
            {searchedProducts.map((product, index) => {
              return (
                <Option
                  key={product.key}
                  className="product-search-one-item-container"
                >
                  <Row align={"middle"} wrap={false}>
                    <Col flex={"50px"}>
                      <img
                        src={product.thumbnail_image}
                        className="product-search-image"
                        alt="searched product image"
                      />
                    </Col>
                    <Col flex={"auto"}>
                      <div
                        style={{
                          whiteSpace: "nowrap",
                          overflow: "hidden",
                          textOverflow: "ellipsis",
                          width: "100%",
                        }}
                      >
                        {product.Brand} {product.description}
                      </div>
                      <div>
                        {product.PackSize} {product.Unit}
                      </div>
                    </Col>
                  </Row>
                </Option>
              );
            })}
          </Select>
        </div>
      </Col>
      {/* <Col xs={8}>
        <NormalButton
          onClick={() => {
            selectProduct();
          }}
          style={{
            height: 40,
            width: "100%",
            maxWidth: "200px",
            backgroundColor: "var(--blueColor)",
            fontSize: 14,
            boxShadow: "0px 0px 20px var(--greyColor)",
          }}
        >
          Search
        </NormalButton>
      </Col> */}
    </Row>
  );
}

export default InputSearchProduct;
