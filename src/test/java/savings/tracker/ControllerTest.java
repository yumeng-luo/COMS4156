package savings.tracker;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {

  @Autowired
  private MockMvc mvc;

  @Test
  public void getFrontend() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/frontend")
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(content().string(equalTo("Placeholder for frontend")));
  }
//
//  @Test
//  public void postSearchValidName() throws Exception {
//    Thread.sleep(2000);
//    mvc.perform(MockMvcRequestBuilders
//        .post("/search").accept(MediaType.APPLICATION_JSON))
//        .andExpect(status().isOk());
//  }
//
//  @Test
//  public void postSearchLongName() throws Exception {
//    Thread.sleep(2000);
//    String longName = "";
//    for (int i = 0; i < 51; i++) {
//      longName += "a";
//    }
//    mvc.perform(MockMvcRequestBuilders.post("/search").param("item", longName)
//        .accept(MediaType.APPLICATION_JSON))
//        .andExpect(content().string(equalTo("[]")));
//  }
//
//  @Test
//  public void postSelectItemValid() throws Exception {
//    Thread.sleep(2000);
//    mvc.perform(MockMvcRequestBuilders.post("/select_item?item_number=0")
//        .accept(MediaType.APPLICATION_JSON))
//        .andExpect(content().json("{'code':200}", false));
//  }
//
//  @Test
//  public void postSelectItemInvaid() throws Exception {
//    Thread.sleep(2000);
//    mvc.perform(MockMvcRequestBuilders.post("/select_item?item_number=-1")
//        .accept(MediaType.APPLICATION_JSON))
//        .andExpect(content().string(equalTo(
//            "{\"code\":400,\"message\":\"Negative index numbers are not allowed.\"}")));
//  }
//
//  @Test
//  public void postalternativesValid() throws Exception {
//    Thread.sleep(2000);
//    mvc.perform(MockMvcRequestBuilders.post("/alternatives")
//        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//  }
//
//  @Test
//  public void postalternativesInvalid() throws Exception {
//    Thread.sleep(2000);
//    //mvc.perform(
//    //    MockMvcRequestBuilders.post("/alternatives?CHEAPER=39?CLOSER=2?SAME=10")
//    //        .accept(MediaType.APPLICATION_JSON))
//    //    .andExpect(status().isBadRequest());
//  }
//
//  @Test
//  public void postSelectPurchaseValidBoundaryCondition1() throws Exception {
//    Thread.sleep(2000);
//    mvc.perform(MockMvcRequestBuilders.post("/select_purchase?lat=0&lon=0")
//        .accept(MediaType.APPLICATION_JSON))
//        .andExpect(content().json("{'code':200}", false));
//  }
//
//  @Test
//  public void postSelectPurchaseValidBoundaryCondition2() throws Exception {
//    Thread.sleep(2000);
//    mvc.perform(MockMvcRequestBuilders.post("/select_purchase?lat=-90&lon=-180")
//        .accept(MediaType.APPLICATION_JSON))
//        .andExpect(content().json("{'code':200}", false));
//  }
//
//  @Test
//  public void postSelectPurchaseValidBoundaryCondition3() throws Exception {
//    Thread.sleep(2000);
//    mvc.perform(MockMvcRequestBuilders.post("/select_purchase?lat=90&lon=-180")
//        .accept(MediaType.APPLICATION_JSON))
//        .andExpect(content().json("{'code':200}", false));
//  }
//
//  @Test
//  public void postSelectPurchaseValidBoundaryCondition4() throws Exception {
//    Thread.sleep(2000);
//    mvc.perform(MockMvcRequestBuilders.post("/select_purchase?lat=-90&lon=180")
//        .accept(MediaType.APPLICATION_JSON))
//        .andExpect(content().json("{'code':200}", false));
//  }
//
//  @Test
//  public void postSelectPurchaseValidBoundaryCondition5() throws Exception {
//    Thread.sleep(2000);
//    mvc.perform(MockMvcRequestBuilders.post("/select_purchase?lat=90&lon=180")
//        .accept(MediaType.APPLICATION_JSON))
//        .andExpect(content().json("{'code':200}", false));
//  }
//
//  @Test
//  public void postSelectPurchaseInvalidBoundaryCondition1() throws Exception {
//    Thread.sleep(2000);
//    mvc.perform(MockMvcRequestBuilders.post("/select_purchase?lat=-91&lon=0")
//        .accept(MediaType.APPLICATION_JSON))
//        .andExpect(content().json("{'code':400}", false));
//  }
//
//  @Test
//  public void postSelectPurchaseInvalidBoundaryCondition2() throws Exception {
//    Thread.sleep(2000);
//    mvc.perform(MockMvcRequestBuilders.post("/select_purchase?lat=0&lon=-181")
//        .accept(MediaType.APPLICATION_JSON))
//        .andExpect(content().json("{'code':400}", false));
//  }
//
//  @Test
//  public void postSelectPurchaseInvalidBoundaryCondition3() throws Exception {
//    Thread.sleep(2000);
//    mvc.perform(MockMvcRequestBuilders.post("/select_purchase?lat=-91&lon=-181")
//        .accept(MediaType.APPLICATION_JSON))
//        .andExpect(content().json("{'code':400}", false));
//  }
//
//  @Test
//  public void postSelectPurchaseInvalidBoundaryCondition4() throws Exception {
//    Thread.sleep(2000);
//    mvc.perform(MockMvcRequestBuilders.post("/select_purchase?lat=91&lon=0")
//        .accept(MediaType.APPLICATION_JSON))
//        .andExpect(content().json("{'code':400}", false));
//  }
//
//  @Test
//  public void postSelectPurchaseInvalidBoundaryCondition5() throws Exception {
//    Thread.sleep(2000);
//    mvc.perform(MockMvcRequestBuilders.post("/select_purchase?lat=0&lon=181")
//        .accept(MediaType.APPLICATION_JSON))
//        .andExpect(content().json("{'code':400}", false));
//  }
//
//  @Test
//  public void postSelectPurchaseInvalidBoundaryCondition6() throws Exception {
//    Thread.sleep(2000);
//    mvc.perform(MockMvcRequestBuilders.post("/select_purchase?lat=91&lon=181")
//        .accept(MediaType.APPLICATION_JSON))
//        .andExpect(content().json("{'code':400}", false));
//  }
//
//  @Test
//  public void postSelectPurchaseInvalidBoundaryCondition7() throws Exception {
//    Thread.sleep(2000);
//    mvc.perform(
//        MockMvcRequestBuilders.post("/select_purchase?lat=-900&lon=-900")
//            .accept(MediaType.APPLICATION_JSON))
//        .andExpect(content().json("{'code':400}", false));
//  }
//
//  /*
//   * @Test public void postAlternatives() throws Exception {
//   * mvc.perform(MockMvcRequestBuilders
//   * .post("/alternatives").accept(MediaType.APPLICATION_JSON))
//   * .andExpect(status().isOk()) .andExpect(content().string(equalTo(
//   * "[{\"name\":\"Milk Life Lact Fr Whole\",\"barcode\":\"7880005592\",\"price\":2.99,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"756505\",\"tcin\":\"7880005592\",\"image\":\"https://wfmproducts.azureedge.net/images-500/078800055926.jpg\"},{\"name\":\"Milk Life Lact Fr Whole\",\"barcode\":\"7880005592\",\"price\":2.99,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"756505\",\"tcin\":\"7880005592\",\"image\":\"https://wfmproducts.azureedge.net/images-500/078800055926.jpg\"},{\"name\":\"Milk Life Lact Fr Whole\",\"barcode\":\"7880005592\",\"price\":2.99,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"756505\",\"tcin\":\"7880005592\",\"image\":\"https://wfmproducts.azureedge.net/images-500/078800055926.jpg\"},{\"name\":\"Milk Life Lact Fr Whole\",\"barcode\":\"7880005592\",\"price\":2.99,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"756505\",\"tcin\":\"7880005592\",\"image\":\"https://wfmproducts.azureedge.net/images-500/078800055926.jpg\"},{\"name\":\"Milk Life Lact Fr Whole\",\"barcode\":\"7880005592\",\"price\":2.99,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"756505\",\"tcin\":\"7880005592\",\"image\":\"https://wfmproducts.azureedge.net/images-500/078800055926.jpg\"},{\"name\":\"Milk Life Lact Fr Whole\",\"barcode\":\"7880005592\",\"price\":2.99,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"756505\",\"tcin\":\"7880005592\",\"image\":\"https://wfmproducts.azureedge.net/images-500/078800055926.jpg\"},{\"name\":\"Milk Life Lact Fr Whole\",\"barcode\":\"7880005592\",\"price\":2.99,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"756505\",\"tcin\":\"7880005592\",\"image\":\"https://wfmproducts.azureedge.net/images-500/078800055926.jpg\"},{\"name\":\"Milk Life Lact Fr Whole\",\"barcode\":\"7880005592\",\"price\":2.99,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"756505\",\"tcin\":\"7880005592\",\"image\":\"https://wfmproducts.azureedge.net/images-500/078800055926.jpg\"},{\"name\":\"Milk Life Lact Fr Whole\",\"barcode\":\"7880005592\",\"price\":2.99,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"756505\",\"tcin\":\"7880005592\",\"image\":\"https://wfmproducts.azureedge.net/images-500/078800055926.jpg\"},{\"name\":\"Milk Life Lact Fr Whole\",\"barcode\":\"7880005592\",\"price\":2.99,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"756505\",\"tcin\":\"7880005592\",\"image\":\"https://wfmproducts.azureedge.net/images-500/078800055926.jpg\"},{\"name\":\"St Mynbrg Goat Milk Whole\",\"barcode\":\"7290400020\",\"price\":8.49,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"474133\",\"tcin\":\"7290400020\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00072904000202.jpg\"},{\"name\":\"St Mynbrg Goat Milk Whole\",\"barcode\":\"7290400020\",\"price\":8.49,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"474133\",\"tcin\":\"7290400020\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00072904000202.jpg\"},{\"name\":\"St Mynbrg Goat Milk Whole\",\"barcode\":\"7290400020\",\"price\":8.49,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"474133\",\"tcin\":\"7290400020\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00072904000202.jpg\"},{\"name\":\"St Mynbrg Goat Milk Whole\",\"barcode\":\"7290400020\",\"price\":8.49,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"474133\",\"tcin\":\"7290400020\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00072904000202.jpg\"},{\"name\":\"St Mynbrg Goat Milk Whole\",\"barcode\":\"7290400020\",\"price\":8.49,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"474133\",\"tcin\":\"7290400020\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00072904000202.jpg\"},{\"name\":\"St Mynbrg Goat Milk Whole\",\"barcode\":\"7290400020\",\"price\":8.49,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"474133\",\"tcin\":\"7290400020\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00072904000202.jpg\"},{\"name\":\"St Mynbrg Goat Milk Whole\",\"barcode\":\"7290400020\",\"price\":8.49,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"474133\",\"tcin\":\"7290400020\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00072904000202.jpg\"},{\"name\":\"St Mynbrg Goat Milk Whole\",\"barcode\":\"7290400020\",\"price\":8.49,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"474133\",\"tcin\":\"7290400020\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00072904000202.jpg\"},{\"name\":\"St Mynbrg Goat Milk Whole\",\"barcode\":\"7290400020\",\"price\":8.49,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"474133\",\"tcin\":\"7290400020\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00072904000202.jpg\"},{\"name\":\"St Mynbrg Goat Milk Whole\",\"barcode\":\"7290400020\",\"price\":8.49,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"474133\",\"tcin\":\"7290400020\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00072904000202.jpg\"}]"
//   * + ""))); }
//   * 
//   * @Test public void postSelectFinal() throws Exception {
//   * mvc.perform(MockMvcRequestBuilders
//   * .post("/select_purchase?upc=7880005592&lat=42.92117&lon=-78.7378")
//   * .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
//   * .andExpect(content().string(
//   * equalTo("{\"code\":200,\"message\":\"Milk Life Lact Fr Whole\"}"))); }
//   */
//
//  @Test
//  public void getNoAlt() throws Exception {
//    mvc.perform(MockMvcRequestBuilders.get("/no_alternative")
//        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
//        .andExpect(content()
//            .string(equalTo("{\"code\":200,\"message\":\"No Alternative\"}")));
//  }

}
