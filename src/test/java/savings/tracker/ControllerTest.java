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

  @Test
  public void postSearch() throws Exception {
    Thread.sleep(20000);
    mvc.perform(MockMvcRequestBuilders
        .post("/search").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo(
            "[{\"name\":\"a2 Whole Milk\",\"barcode\":\"81326702000\",\"price\":3.99,\"store\":\"Wegmans Store\",\"lat\":42.06996,\"lon\":-80.1919,\"sku\":\"731450\",\"tcin\":\"81326702000\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00813267020007.jpg\"},{\"name\":\"a2 Whole Milk\",\"barcode\":\"81326702000\",\"price\":3.99,\"store\":\"Wegmans Store\",\"lat\":42.0602,\"lon\":-80.0907,\"sku\":\"731450\",\"tcin\":\"81326702000\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00813267020007.jpg\"},{\"name\":\"a2 Whole Milk\",\"barcode\":\"81326702000\",\"price\":3.99,\"store\":\"Wegmans Store\",\"lat\":42.09373,\"lon\":-79.29009,\"sku\":\"731450\",\"tcin\":\"81326702000\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00813267020007.jpg\"},{\"name\":\"Homestead Whole Milk Qt\",\"barcode\":\"87525200015\",\"price\":2.59,\"store\":\"Wegmans Store\",\"lat\":42.06996,\"lon\":-80.1919,\"sku\":\"723538\",\"tcin\":\"87525200015\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00875252000159.jpg\"},{\"name\":\"Homestead Whole Milk Qt\",\"barcode\":\"87525200015\",\"price\":2.59,\"store\":\"Wegmans Store\",\"lat\":42.0602,\"lon\":-80.0907,\"sku\":\"723538\",\"tcin\":\"87525200015\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00875252000159.jpg\"},{\"name\":\"Homestead Whole Milk Qt\",\"barcode\":\"87525200015\",\"price\":2.59,\"store\":\"Wegmans Store\",\"lat\":42.09373,\"lon\":-79.29009,\"sku\":\"723538\",\"tcin\":\"87525200015\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00875252000159.jpg\"},{\"name\":\"Lactaid Whole Milk\",\"barcode\":\"4138309073\",\"price\":5.29,\"store\":\"Wegmans Store\",\"lat\":42.06996,\"lon\":-80.1919,\"sku\":\"465140\",\"tcin\":\"4138309073\",\"image\":\"https://wfmproducts.azureedge.net/images-500/041383090738.jpg\"},{\"name\":\"Lactaid Whole Milk\",\"barcode\":\"4138309073\",\"price\":5.29,\"store\":\"Wegmans Store\",\"lat\":42.0602,\"lon\":-80.0907,\"sku\":\"465140\",\"tcin\":\"4138309073\",\"image\":\"https://wfmproducts.azureedge.net/images-500/041383090738.jpg\"},{\"name\":\"Lactaid Whole Milk\",\"barcode\":\"4138309073\",\"price\":4.49,\"store\":\"Wegmans Store\",\"lat\":42.09373,\"lon\":-79.29009,\"sku\":\"465140\",\"tcin\":\"4138309073\",\"image\":\"https://wfmproducts.azureedge.net/images-500/041383090738.jpg\"},{\"name\":\"CV Parmalat Whole Milk\",\"barcode\":\"85706500701\",\"price\":2.29,\"store\":\"Wegmans Store\",\"lat\":42.06996,\"lon\":-80.1919,\"sku\":\"732080\",\"tcin\":\"85706500701\",\"image\":\"https://wfmproducts.azureedge.net/images-500/857065007013.jpg\"},{\"name\":\"CV Parmalat Whole Milk\",\"barcode\":\"85706500701\",\"price\":2.29,\"store\":\"Wegmans Store\",\"lat\":42.0602,\"lon\":-80.0907,\"sku\":\"732080\",\"tcin\":\"85706500701\",\"image\":\"https://wfmproducts.azureedge.net/images-500/857065007013.jpg\"},{\"name\":\"CV Parmalat Whole Milk\",\"barcode\":\"85706500701\",\"price\":2.29,\"store\":\"Wegmans Store\",\"lat\":42.09373,\"lon\":-79.29009,\"sku\":\"732080\",\"tcin\":\"85706500701\",\"image\":\"https://wfmproducts.azureedge.net/images-500/857065007013.jpg\"},{\"name\":\"Lactaid 100% Whole Milk\",\"barcode\":\"4138309036\",\"price\":3.99,\"store\":\"Wegmans Store\",\"lat\":42.06996,\"lon\":-80.1919,\"sku\":\"126383\",\"tcin\":\"4138309036\",\"image\":\"https://wfmproducts.azureedge.net/images-500/041383090363.jpg\"},{\"name\":\"Lactaid 100% Whole Milk\",\"barcode\":\"4138309036\",\"price\":3.99,\"store\":\"Wegmans Store\",\"lat\":42.0602,\"lon\":-80.0907,\"sku\":\"126383\",\"tcin\":\"4138309036\",\"image\":\"https://wfmproducts.azureedge.net/images-500/041383090363.jpg\"},{\"name\":\"Lactaid 100% Whole Milk\",\"barcode\":\"4138309036\",\"price\":3.79,\"store\":\"Wegmans Store\",\"lat\":42.09373,\"lon\":-79.29009,\"sku\":\"126383\",\"tcin\":\"4138309036\",\"image\":\"https://wfmproducts.azureedge.net/images-500/041383090363.jpg\"}]"
        + "")));
  }

  @Test
  public void postSelect() throws Exception {

  }

  /*
   * @Test public void postAlternatives() throws Exception {
   * mvc.perform(MockMvcRequestBuilders
   * .post("/alternatives").accept(MediaType.APPLICATION_JSON))
   * .andExpect(status().isOk()) .andExpect(content().string(equalTo(
   * "[{\"name\":\"Milk Life Lact Fr Whole\",\"barcode\":\"7880005592\",\"price\":2.99,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"756505\",\"tcin\":\"7880005592\",\"image\":\"https://wfmproducts.azureedge.net/images-500/078800055926.jpg\"},{\"name\":\"Milk Life Lact Fr Whole\",\"barcode\":\"7880005592\",\"price\":2.99,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"756505\",\"tcin\":\"7880005592\",\"image\":\"https://wfmproducts.azureedge.net/images-500/078800055926.jpg\"},{\"name\":\"Milk Life Lact Fr Whole\",\"barcode\":\"7880005592\",\"price\":2.99,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"756505\",\"tcin\":\"7880005592\",\"image\":\"https://wfmproducts.azureedge.net/images-500/078800055926.jpg\"},{\"name\":\"Milk Life Lact Fr Whole\",\"barcode\":\"7880005592\",\"price\":2.99,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"756505\",\"tcin\":\"7880005592\",\"image\":\"https://wfmproducts.azureedge.net/images-500/078800055926.jpg\"},{\"name\":\"Milk Life Lact Fr Whole\",\"barcode\":\"7880005592\",\"price\":2.99,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"756505\",\"tcin\":\"7880005592\",\"image\":\"https://wfmproducts.azureedge.net/images-500/078800055926.jpg\"},{\"name\":\"Milk Life Lact Fr Whole\",\"barcode\":\"7880005592\",\"price\":2.99,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"756505\",\"tcin\":\"7880005592\",\"image\":\"https://wfmproducts.azureedge.net/images-500/078800055926.jpg\"},{\"name\":\"Milk Life Lact Fr Whole\",\"barcode\":\"7880005592\",\"price\":2.99,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"756505\",\"tcin\":\"7880005592\",\"image\":\"https://wfmproducts.azureedge.net/images-500/078800055926.jpg\"},{\"name\":\"Milk Life Lact Fr Whole\",\"barcode\":\"7880005592\",\"price\":2.99,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"756505\",\"tcin\":\"7880005592\",\"image\":\"https://wfmproducts.azureedge.net/images-500/078800055926.jpg\"},{\"name\":\"Milk Life Lact Fr Whole\",\"barcode\":\"7880005592\",\"price\":2.99,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"756505\",\"tcin\":\"7880005592\",\"image\":\"https://wfmproducts.azureedge.net/images-500/078800055926.jpg\"},{\"name\":\"Milk Life Lact Fr Whole\",\"barcode\":\"7880005592\",\"price\":2.99,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"756505\",\"tcin\":\"7880005592\",\"image\":\"https://wfmproducts.azureedge.net/images-500/078800055926.jpg\"},{\"name\":\"St Mynbrg Goat Milk Whole\",\"barcode\":\"7290400020\",\"price\":8.49,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"474133\",\"tcin\":\"7290400020\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00072904000202.jpg\"},{\"name\":\"St Mynbrg Goat Milk Whole\",\"barcode\":\"7290400020\",\"price\":8.49,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"474133\",\"tcin\":\"7290400020\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00072904000202.jpg\"},{\"name\":\"St Mynbrg Goat Milk Whole\",\"barcode\":\"7290400020\",\"price\":8.49,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"474133\",\"tcin\":\"7290400020\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00072904000202.jpg\"},{\"name\":\"St Mynbrg Goat Milk Whole\",\"barcode\":\"7290400020\",\"price\":8.49,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"474133\",\"tcin\":\"7290400020\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00072904000202.jpg\"},{\"name\":\"St Mynbrg Goat Milk Whole\",\"barcode\":\"7290400020\",\"price\":8.49,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"474133\",\"tcin\":\"7290400020\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00072904000202.jpg\"},{\"name\":\"St Mynbrg Goat Milk Whole\",\"barcode\":\"7290400020\",\"price\":8.49,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"474133\",\"tcin\":\"7290400020\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00072904000202.jpg\"},{\"name\":\"St Mynbrg Goat Milk Whole\",\"barcode\":\"7290400020\",\"price\":8.49,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"474133\",\"tcin\":\"7290400020\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00072904000202.jpg\"},{\"name\":\"St Mynbrg Goat Milk Whole\",\"barcode\":\"7290400020\",\"price\":8.49,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"474133\",\"tcin\":\"7290400020\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00072904000202.jpg\"},{\"name\":\"St Mynbrg Goat Milk Whole\",\"barcode\":\"7290400020\",\"price\":8.49,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"474133\",\"tcin\":\"7290400020\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00072904000202.jpg\"},{\"name\":\"St Mynbrg Goat Milk Whole\",\"barcode\":\"7290400020\",\"price\":8.49,\"store\":\"Wegmans Store\",\"lat\":35.821138,\"lon\":-78.621264,\"sku\":\"474133\",\"tcin\":\"7290400020\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00072904000202.jpg\"}]"
   * + ""))); }
   * 
   * @Test public void postSelectFinal() throws Exception {
   * mvc.perform(MockMvcRequestBuilders
   * .post("/select_purchase?upc=7880005592&lat=42.92117&lon=-78.7378")
   * .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
   * .andExpect(content().string(
   * equalTo("{\"code\":200,\"message\":\"Milk Life Lact Fr Whole\"}"))); }
   */

  @Test
  public void getNoAlt() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/no_alternative")
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(content()
            .string(equalTo("{\"code\":200,\"message\":\"No Alternative\"}")));
  }

}