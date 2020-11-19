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
    mvc.perform(MockMvcRequestBuilders.post("/search")
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(content().string(equalTo(
            "[{\"name\":\"Ithaca Seasonal Coffee\",\"barcode\":\"9492244571\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"635225\",\"tcin\":\"9492244571\"},{\"name\":\"Peet's Coffee Sumatra Grd\",\"barcode\":\"78535783906\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"214525\",\"tcin\":\"78535783906\"},{\"name\":\"Peet's Coffee House Grnd\",\"barcode\":\"78535783526\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"399394\",\"tcin\":\"78535783526\"},{\"name\":\"Peet's Coffee House WB\",\"barcode\":\"78535710052\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"138366\",\"tcin\":\"78535710052\"},{\"name\":\"Ithaca Gorges Blend\",\"barcode\":\"9492235032\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"635232\",\"tcin\":\"9492235032\"},{\"name\":\"CV Ella Coffee-Luna Drk\",\"barcode\":\"73038517790\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"766609\",\"tcin\":\"73038517790\"},{\"name\":\"Fnger OG WB Papua Coffee\",\"barcode\":\"89355400101\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"162431\",\"tcin\":\"89355400101\"},{\"name\":\"Ithaca Org Mexico Chiapas\",\"barcode\":\"9492244573\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"633974\",\"tcin\":\"9492244573\"},{\"name\":\"CV Ella Coffee-2Maria Med\",\"barcode\":\"73038517792\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"766617\",\"tcin\":\"73038517792\"},{\"name\":\"Fnger OG WB Break Coffee\",\"barcode\":\"89355400100\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"180441\",\"tcin\":\"89355400100\"},{\"name\":\"Fnger OG WB Ncrgn Coffee\",\"barcode\":\"89355400102\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"162818\",\"tcin\":\"89355400102\"},{\"name\":\"Ithaca Org Columbia Coffe\",\"barcode\":\"9492235025\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"635235\",\"tcin\":\"9492235025\"},{\"name\":\"CV-Recess Kenyan Coffee\",\"barcode\":\"2819400582\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"828374\",\"tcin\":\"2819400582\"},{\"name\":\"Fnger OG WB Sumtra Coffee\",\"barcode\":\"89355400104\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"180420\",\"tcin\":\"89355400104\"},{\"name\":\"Fnger OG WB Fr Rst Coffee\",\"barcode\":\"89355400106\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"180487\",\"tcin\":\"89355400106\"},{\"name\":\"Coffee-Mate Hazelnut SF\",\"barcode\":\"5000034934\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"279493\",\"tcin\":\"5000034934\"},{\"name\":\"CV-REC Ethiopian Coffee\",\"barcode\":\"2819400579\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"828370\",\"tcin\":\"2819400579\"},{\"name\":\"Fasigs Coffee Cake Coffee\",\"barcode\":\"4928416052\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"137755\",\"tcin\":\"4928416052\"},{\"name\":\"Ithaca OG Cornl Blnd Grnd\",\"barcode\":\"9492200846\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"395929\",\"tcin\":\"9492200846\"},{\"name\":\"Peets Holiday Blnd whl bn\",\"barcode\":\"78535701450\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"636791\",\"tcin\":\"78535701450\"},{\"name\":\"Larry's Coffee Whl Bean\",\"barcode\":\"81764300039\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"828796\",\"tcin\":\"81764300039\"},{\"name\":\"CV-Recess Austin's Coffee\",\"barcode\":\"2819400581\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"828368\",\"tcin\":\"2819400581\"},{\"name\":\"Peet's Brazil Coffee\",\"barcode\":\"78535702245\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"790683\",\"tcin\":\"78535702245\"},{\"name\":\"Peet's Sumatra Coffee\",\"barcode\":\"78535702246\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"790678\",\"tcin\":\"78535702246\"},{\"name\":\"Peets Holiday Blnd Gr Cof\",\"barcode\":\"78535701451\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"636792\",\"tcin\":\"78535701451\"},{\"name\":\"SQ1 Coffee Colombia WB\",\"barcode\":\"75283024099\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"755638\",\"tcin\":\"75283024099\"},{\"name\":\"SQ1 Coffee Nightcap WB\",\"barcode\":\"75283078898\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"755639\",\"tcin\":\"75283078898\"},{\"name\":\"Stedman Chautauqua Coffee\",\"barcode\":\"67947500015\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"747159\",\"tcin\":\"67947500015\"},{\"name\":\"SQ1 Coffee Sasquatch WB\",\"barcode\":\"75283078888\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"755642\",\"tcin\":\"75283078888\"},{\"name\":\"CV-Recess Decaf WB Coffee\",\"barcode\":\"2819400583\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"828371\",\"tcin\":\"2819400583\"},{\"name\":\"Stedman Noir WB Coffee\",\"barcode\":\"67947500024\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"747165\",\"tcin\":\"67947500024\"},{\"name\":\"Peet's Ethiopia Coffee\",\"barcode\":\"78535702243\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"790668\",\"tcin\":\"78535702243\"},{\"name\":\"Peet's Big Bang Ground\",\"barcode\":\"78535701509\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"654261\",\"tcin\":\"78535701509\"},{\"name\":\"CX Atomic Black Velvet\",\"barcode\":\"89669800143\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"710805\",\"tcin\":\"89669800143\"},{\"name\":\"CX Atomic Diesel Dark Rst\",\"barcode\":\"89669800105\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"710811\",\"tcin\":\"89669800105\"},{\"name\":\"SQ1 Coffee Hexproof WB\",\"barcode\":\"75283078868\",\"price\":0.0,\"store\":\"Wegmans\",\"lat\":0.0,\"lon\":0.0,\"sku\":\"755644\",\"tcin\":\"75283078868\"}]")));
  }

  @Test
  public void postSelect() throws Exception {
    mvc.perform(MockMvcRequestBuilders.post("/select_item")
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(content()
            .string(equalTo("{\"code\":200,\"message\":\"Ithaca Seasonal Coffee\"}")));
  }
  

  @Test
  public void postSelectFinal() throws Exception {
    mvc.perform(MockMvcRequestBuilders.post("/select_purchase?upc=9492244571")
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(content()
            .string(equalTo("{\"code\":200,\"message\":\"Ithaca Seasonal Coffee\"}")));
  }
  

  @Test
  public void getNoAlt() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/no_alternative")
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(content()
            .string(equalTo("{\"code\":200,\"message\":\"No Alternative\"}")));
  }

}