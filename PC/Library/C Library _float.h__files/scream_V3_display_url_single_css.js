/**
 * @fileoverview Scream V3 javascript.
 */

var elements = {
  adContent: {
    type: 'shape',
    node_id: 'adContent',
    background_color_ids: 'textBackLightColor'
  },
  colorSlot1: {
    type: 'shape',
    node_id: 'color-slot1',
    background_color_ids: 'logoBackColor'
  },
  colorSlot2: {
    type: 'shape',
    node_id: 'color-slot2',
    background_color_ids: 'textBackDarkColor'
  },
  product1MCImage: {
    type: 'product',
    node_id: 'product-image',
  },
  text1TFText: {
    type: 'headline',
    node_id: 'headline',
    background_color_ids: 'textBackDarkColor'
  },
  text2TFText: {
    type: 'description',
    node_id: 'description',
    background_color_ids: 'textBackLightColor'
  },
  displayUrl: {
    type: 'url',
    node_id: 'display-url',
    background_color_ids: 'textBackLightColor'
  },
  clickTFText1: {
    field_name: 'clickTFText',
    type: 'button',
    node_id: 'button1',
    background_color_ids: 'textBackDarkColor',
    weight: 1 / 28.0
  },
  clickTFText2: {
    field_name: 'clickTFText',
    type: 'button',
    node_id: 'button2',
    background_color_ids: 'logoBackColor',
    weight: 1 / 28.0
  }
};

var spec = {
  elements: elements,
  font_scale_strategy: 'mega_title',
  variations: {
    square1: {
      calibrations: [
        'AspectRatioGE 2 0',
        'AspectRatioLE 0.5 0'
      ],
      styles: {
        clickTFText1: {
          displayType: 'nessieButton'
        },
        text1TFText: {
          padding: '15px 0%'
        },
        text2TFText: {
          padding: '10% 0%'
        }
      },
      logo: {
        type: 'element',
        spec: {
          element: 'product1MCImage'
        },
        alignments: 'top left',
        right: '30%',
        bottom: '65%'
      },
      colorSlot1: {
        type: 'element',
        spec: {
          element: 'colorSlot1'
        },
        top: '0',
        left: '0',
        right: '0',
        bottom: 'text1 0'
      },
      text1: {
        type: 'element',
        spec: {
          element: 'text1TFText'
        },
        alignments: 'left top',
        top: 'logo',
        bottom: 'text1 30%'
      },
      colorSlot2: {
        type: 'element',
        spec: {
          element: 'colorSlot2'
        },
        top: 'text1 top 0',
        left: '0',
        right: '0',
        bottom: 'text1 bottom 0',
      },
      text2: {
        type: 'element',
        spec: {
          element: 'text2TFText'
        },
        alignments: 'left',
        top: 'text1 5px',
        right: 'button',
        bottom: 'url 0'
      },
      url: {
        type: 'element',
        spec: {
          element: 'displayUrl'
        },
        alignments: 'left bottom',
        top: 'url 30px',
        bottom: '5px',
        right: 'button',
      },
      button: {
        type: 'element',
        spec: {
          element: 'clickTFText1'
        },
        alignments: 'right',
        top: 'text1 5px',
        left: '50%',
        bottom: '5px'
      }
    },
    square2: {
      calibrations: [
        'AspectRatioGE 2.5 0',
        'AspectRatioLE 1.5 0'
      ],
      styles: {
        clickTFText1: {
          displayType: 'nessieButton'
        },
        text1TFText: {
          padding: '0% 10px'
        },
        text2TFText: {
          padding: '10px 0 0 0'
        }
      },
      logo: {
        type: 'element',
        spec: {
          element: 'product1MCImage'
        },
        alignments: 'top left',
        right: '50%',
        bottom: 'min(30%, 60px)'
      },
      colorSlot1: {
        type: 'element',
        spec: {
          element: 'colorSlot1'
        },
        top: '0',
        left: '0',
        right: 'text1 0',
        bottom: 'text2 0'
      },
      text1: {
        type: 'element',
        spec: {
          element: 'text1TFText'
        },
        alignments: 'left right top bottom',
        left: 'logo',
        bottom: 'logo bottom 0px'
      },
      colorSlot2: {
        type: 'element',
        spec: {
          element: 'colorSlot2'
        },
        top: '0',
        left: 'logo',
        right: '0',
        bottom: 'text2 0',
      },
      text2: {
        type: 'element',
        spec: {
          element: 'text2TFText'
        },
        alignments: 'left top bottom',
        top: 'logo',
        right: 'button',
        bottom: 'url 5px'
      },
      url: {
        type: 'element',
        spec: {
          element: 'displayUrl'
        },
        alignments: 'left bottom',
        top: 'url 30px',
        bottom: '5px',
        right: 'button',
      },
      button: {
        type: 'element',
        spec: {
          element: 'clickTFText1'
        },
        alignments: 'right',
        top: 'logo',
        left: '50%',
        bottom: '0px'
      }
    },
    banner: {
      calibrations: [
        'AspectRatioLE 2 0'
      ],
      styles: {
        clickTFText2: {
          displayType: 'nessieButton'
        },
        text1TFText: {
          padding: '10% 5%'
        },
        text2TFText: {
          padding: '0 15px 0 15px'
        }
      },
      logo: {
        type: 'element',
        spec: {
          element: 'product1MCImage'
        },
        alignments: 'left',
        left: '10px',
        right: '70%'
      },
      colorSlot1: {
        type: 'element',
        spec: {
          element: 'colorSlot1'
        },
        top: '0',
        left: '0',
        right: 'text1 10px',
        bottom: '0'
      },
      text1: {
        type: 'element',
        spec: {
          element: 'text1TFText'
        },
        alignments: 'left top bottom',
        left: 'logo 20px',
        right: '40%'
      },
      colorSlot2: {
        type: 'element',
        spec: {
          element: 'colorSlot2'
        },
        top: '0',
        left: 'logo 10px',
        right: 'text2 10px',
        bottom: '0'
      },
      text2: {
        type: 'element',
        spec: {
          element: 'text2TFText'
        },
        alignments: 'top bottom left',
        left: 'text1 10px',
        right: '70px',
        bottom: 'url 2%'
      },
      button: {
        type: 'element',
        spec: {
          element: 'clickTFText2'
        },
        alignments: 'top bottom',
        top: '5%',
        bottom: '5%',
        left: 'text2'
      },
      url: {
        type: 'element',
        spec: {
          element: 'displayUrl'
        },
        alignments: 'left bottom',
        top: 'url 30px',
        left: 'text1 25px',
        right: '70px',
        bottom: '5px'
      }
    },
    tower: {
      calibrations: [
        'AspectRatioGE 0.8 0'
      ],
      styles: {
        clickTFText1: {
          displayType: 'nessieButton'
        },
        text1TFText: {
          padding: '20% 0%'
        }
      },
      logo: {
        type: 'element',
        spec: {
          element: 'product1MCImage'
        },
        alignments: 'top',
        bottom: '70%'
      },
      colorSlot1: {
        type: 'element',
        spec: {
          element: 'colorSlot1'
        },
        top: '0',
        left: '0',
        right: '0',
        bottom: 'text1 0'
      },
      text1: {
        type: 'element',
        spec: {
          element: 'text1TFText'
        },
        alignments: 'left right top',
        top: 'logo',
        bottom: '40%',
        left: '10%',
        right: '10%'
      },
      colorSlot2: {
        type: 'element',
        spec: {
          element: 'colorSlot2'
        },
        top: 'text1 top 0',
        left: '0',
        right: '0',
        bottom: 'text1 bottom 0',
      },
      text2: {
        type: 'element',
        spec: {
          element: 'text2TFText'
        },
        alignments: 'left right top bottom',
        top: 'text1 2%',
        bottom: 'button 2%',
        left: '10%',
        right: '10%'
      },
      button: {
        type: 'element',
        spec: {
          element: 'clickTFText1'
        },
        alignments: 'left right bottom',
        bottom: 'url 5%',
        top: '70%'
      },
      url: {
        type: 'element',
        spec: {
          element: 'displayUrl'
        },
        alignments: 'bottom',
        top: 'url 30px',
        bottom: '5%'
      }
    }
  }
};

var smallSizeSpec = {
  elements: elements,
  variations: {
    tower_small: {
      calibrations: [
        'AspectRatioGE 2 0'
      ],
      styles: {
        clickTFText2: {
          displayType: 'nessieButton'
        },
        displayUrl: {
          font_level: 0
        }
      },
      product1MCImage: {
        type: 'element',
        spec: {
          element: 'product1MCImage'
        },
        alignments: 'top left right bottom',
        top: '3%',
        bottom: '63%'
      },
      textBox1: {
        type: 'element',
        spec: {
          element: 'text1TFText'
        },
        top: '43%',
        bottom: 'button 3%',
        left: '7%',
        right: '7%'
      },
      textBox2: {
        type: 'element',
        spec: {
          element: 'text2TFText'
        },
        top: '43%',
        bottom: 'button 3%',
        left: '7%',
        right: '7%',
        z_index: 1
      },
      colorSlot1: {
        type: 'element',
        spec: {
          element: 'colorSlot1'
        },
        top: '0',
        left: '0',
        right: '0',
        bottom: '60%'
      },
      url: {
        type: 'element',
        spec: {
          element: 'displayUrl'
        },
        top: '43%',
        bottom: 'button 3%',
        left: '7%',
        right: '7%',
        z_index: 2
      },
      button: {
        type: 'element',
        spec: {
          element: 'clickTFText2'
        },
        alignments: 'bottom',
        top: '70%'
      }
    },
    banner_small: {
      calibrations: [
        'AspectRatioLE 0.5 0'
      ],
      styles: {
        clickTFText2: {
          displayType: 'nessieButton'
        },
        displayUrl: {
          font_level: 0
        }
      },
      product1MCImage: {
        type: 'element',
        spec: {
          element: 'product1MCImage'
        },
        alignments: 'top left right bottom',
        left: '0%',
        right: '75%'
      },
      colorSlot1: {
        type: 'element',
        spec: {
          element: 'colorSlot1'
        },
        top: '0',
        left: '0',
        right: '75%',
        bottom: '0'
      },
      text1: {
        type: 'element',
        spec: {
          element: 'text1TFText'
        },
        top: '10px',
        bottom: '10px',
        left: '27%',
        right: 'button 2%'
      },
      text2: {
        type: 'element',
        spec: {
          element: 'text2TFText'
        },
        top: '2%',
        bottom: '2%',
        left: '27%',
        right: 'button 2%',
        z_index: 1
      },
      url: {
        type: 'element',
        spec: {
          element: 'displayUrl'
        },
        top: '12px',
        bottom: '12px',
        left: '30%',
        right: 'button 5%',
        z_index: 2
      },
      button: {
        type: 'element',
        spec: {
          element: 'clickTFText2'
        },
        alignments: 'right',
        left: '70%'
      }
    }
  }
};

function onAdData(adData) {
  var ccm = adData['google_template_data']['adData'][0];
  ccm['siriusFlagEnableSingleIframeRendering'] = 'true';

  // TODO(zhangguannan): Remove this after we unified field names for preview
  // and serving.
  if (!ccm['product1MCImage'] && ccm['logoMCImage']) {
    ccm['product1MCImage'] = ccm['logoMCImage'];
  }

  sirius.adsettings.setupRicherTextAdsClick(adData);
  sirius.adsettings.setupScreamV3Colors(adData);
  ccm['siriusFlagIntentfulClickDelay'] = 1000;

  var pickedSpec = isSmallSizeAd(adData) ? smallSizeSpec : spec;
  renderAd(adData, pickedSpec, function() {decorateLayout(adData);});
}

function decorateLayout(adData) {
  var adContent = document.getElementById('adContent');
  var variation = adContent.getAttribute('data-variation');
  var product = document.getElementById('product-image');
  var description = document.getElementById('description');
  var displayUrl = document.getElementById('display-url');
  var colorSlot1 = document.getElementById('color-slot1');

  var ccm = adData['google_template_data']['adData'][0];
  if (parseInt(ccm['logoColorType']) == 2) {
     product.className += ' photo';
  }

  if (variation == 'tower_small' || variation == 'banner_small') {
    adContent.style.background = ccm['textBackDarkColor'];
    colorSlot1.style.background = ccm['logoBackColor'];
    var text1Color = getSiriusTextColor(elements['text1TFText'], adData);
    description.style.color = text1Color;
    displayUrl.style.color = text1Color;
  }
}
