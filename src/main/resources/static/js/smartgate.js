// Check if jQuery is not already defined
if (typeof jQuery === 'undefined') {
    // Dynamically load jQuery
    var script = document.createElement('script');
    script.src = "/js/jquery-3.6.0.js";
    script.type = 'text/javascript';
    script.onload = function() {
        console.log("jQuery loaded successfully.");
        // Your jQuery-dependent code here
    };
    document.getElementsByTagName('head')[0].appendChild(script);
}

  function toggleHistorySubmenu(event) {
    event.preventDefault();
    const submenu = document.getElementById('historySubmenu');
    const chevron = document.getElementById('historyChevron');
    const sidebar = document.getElementById('sidebar');

    submenu.classList.toggle('hidden');
    chevron.classList.toggle('rotate-180');

    // Adjust alignment when minimized
    if (sidebar.classList.contains('minimized')) {
        submenu.style.position = 'absolute';
        submenu.style.left = '100%';
        submenu.style.top = '0';
        submenu.style.width = '200px'; // Adjust width as needed
        submenu.style.backgroundColor = '#1F2937';
        submenu.style.boxShadow = '2px 0 5px rgba(0,0,0,0.1)';
        submenu.style.zIndex = '1000';
        submenu.style.display = submenu.classList.contains('hidden') ? 'none' : 'block';

        const submenuItems = submenu.querySelectorAll('a');
        submenuItems.forEach(item => {
            item.classList.add('flex', 'items-center');
            item.classList.remove('block');
            item.style.padding = '0.5rem';
            item.style.whiteSpace = 'nowrap';
            
            // Keep text visible
            const span = item.querySelector('.submenu-text');
            if (span) {
                span.style.display = 'inline';
                span.style.marginLeft = '0.5rem';
                span.style.opacity = '1';
                span.style.visibility = 'visible';
            }
        });

        // Show submenu on hover
        const historyItem = document.querySelector('.sidebar-nav-item');
        historyItem.addEventListener('mouseenter', function() {
            if (sidebar.classList.contains('minimized') && !submenu.classList.contains('hidden')) {
                submenu.style.display = 'block';
            }
        });
        historyItem.addEventListener('mouseleave', function() {
            if (sidebar.classList.contains('minimized')) {
                submenu.style.display = 'none';
            }
        });
    } else {
        submenu.style.position = 'static';
        submenu.style.left = 'auto';
        submenu.style.top = 'auto';
        submenu.style.width = '100%';
        submenu.style.backgroundColor = 'transparent';
        submenu.style.boxShadow = 'none';
        submenu.style.zIndex = 'auto';
        submenu.style.display = submenu.classList.contains('hidden') ? 'none' : 'block';

        // Reset alignment for expanded state
        const submenuItems = submenu.querySelectorAll('a');
        submenuItems.forEach(item => {
            item.classList.remove('flex', 'items-center');
            item.classList.add('block');
            item.style.padding = '0.5rem 1rem';
            item.style.whiteSpace = 'normal';
            
            // Ensure text is visible
            const span = item.querySelector('.submenu-text');
            if (span) {
                span.style.display = 'inline';
                span.style.marginLeft = '0';
                span.style.opacity = '1';
                span.style.visibility = 'visible';
            }
        });
    }
}

// Prevent event bubbling for submenu items
document.querySelectorAll('#historySubmenu a').forEach(item => {
    item.addEventListener('click', function(e) {
        e.stopPropagation();
    });
});

// Ensure submenu text is always visible
document.addEventListener('DOMContentLoaded', function() {
    const sidebar = document.getElementById('sidebar');
    const submenuTexts = document.querySelectorAll('.submenu-text');

    function updateSubmenuTextVisibility() {
        submenuTexts.forEach(text => {
            text.style.display = 'inline';
            text.style.opacity = '1';
            text.style.visibility = 'visible';
        });
    }

    // Initial call
    updateSubmenuTextVisibility();

    // Update on sidebar toggle
    sidebar.addEventListener('transitionend', updateSubmenuTextVisibility);
});

$(document).ready(function (){
				 function fetchNotif() {
        $.ajax({
            url: '/getNotifications',
            method: 'GET',
            success: function(data) {
                var hasNotif = data.hasNotif;

            // Log or update the UI with the notification status
            console.log("Has Notif Value: ", hasNotif);

            if (hasNotif > 0) {
                $('#hasNotif').css('display', 'block'); // Show the hasNotif div
                $('#noNotif').css('display', 'none');   // Hide the noNotif div
                $('#notifText').text(hasNotif);
            } else {
                $('#hasNotif').css('display', 'none');  // Hide the hasNotif div
                $('#noNotif').css('display', 'block');  // Show the noNotif div
            }
            },
            error: function(error) {
                console.log("errorrrr");
            }
        });
    }
  
		fetchNotif()


        setInterval(fetchNotif, 5000);
			});
    let html5QrCode;

    function onScanSuccess(decodedText, decodedResult) {
        // Display the scanned text
       // document.getElementById("qr-result").innerHTML = `<strong>Scanned QR Code:</strong> ${decodedText}`;

        // Stop the scanning
        if (html5QrCode) {
            html5QrCode.stop().then(() => {
                console.log("QR scanning stopped.");
            }).catch(err => {
                console.error("Error stopping the scanner: ", err);
            });
        }

        // Set the scanned code in the hidden form input and submit the form
        document.getElementById("qrCode").value = decodedText;
        document.getElementById("qrForm").submit();  // Automatically submit the form
    }

    function onScanFailure(error) {
        // Log errors for debugging (optional)
        console.warn(`Code scan error: ${error}`);
    }

    function startQRScanner() {
        html5QrCode = new Html5Qrcode("qr-reader");
        html5QrCode.start(
            { facingMode: "environment" },  // Use "user" for front camera
            {
                fps: 5,  // Lower FPS to help the camera focus better
                qrbox: { width: 250, height: 250 }  // Adjust QR scanning box size
            },
            onScanSuccess,
            onScanFailure
        ).catch(err => {
            console.error(`Error starting QR scanner: ${err}`);
        });
    }
	var varCommand;

function command(commandVar) {
    console.log("commmannndsss: " + commandVar);
    varCommand = commandVar;
    manualOpenClose();
}

function manualOpenClose() {
    console.log(varCommand + "hahahahha" + varCommand);
    $.ajax({
        type: 'POST',
        url: '/manualOpenClose',
        data: { command: varCommand },
        success: function(response) {
            $('#response').text(response);
            console.log('Server Response:', response);
        },
        error: function(error) {
            console.error('Error:', error);
        }
    });
}

function showContent(contentType) {
    const entryContent = document.getElementById('entryContent');
    const exitContent = document.getElementById('exitContent');
    const entryTab = document.getElementById('entryTab');
    const exitTab = document.getElementById('exitTab');

    if (contentType === 'entry') {
        entryContent.classList.remove('hidden');
        exitContent.classList.add('hidden');
        entryTab.classList.add('bg-purple-600', 'text-white');
        entryTab.classList.remove('bg-gray-200', 'text-gray-700');
        exitTab.classList.add('bg-gray-200', 'text-gray-700');
        exitTab.classList.remove('bg-purple-600', 'text-white');
    } else {
        entryContent.classList.add('hidden');
        exitContent.classList.remove('hidden');
        exitTab.classList.add('bg-purple-600', 'text-white');
        exitTab.classList.remove('bg-gray-200', 'text-gray-700');
        entryTab.classList.add('bg-gray-200', 'text-gray-700');
        entryTab.classList.remove('bg-purple-600', 'text-white');
    }
}

  document.addEventListener('DOMContentLoaded', function() {
    const exitRfid1 = document.getElementById('exitRfid1');
    const exitRfid2 = document.getElementById('exitRfid2');

    function formatDateTime(date) {
        const optionsDate = { year: 'numeric', month: 'long', day: '2-digit' };
        const optionsTime = { hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: true };
        
        const formattedDate = date.toLocaleDateString('en-US', optionsDate);
        const formattedTime = date.toLocaleTimeString('en-US', optionsTime);
        
        return {
            date: formattedDate,
            time: formattedTime
        };
    }


    function checkInputs() {
        // Move focus to the second input if the first one has 12 characters
        if (exitRfid1.value.length === 12) {
            // Store the current date and time in local storage
            const now = new Date();
            const formattedDateTime = formatDateTime(now);
            localStorage.setItem('rfid1Timestamp', JSON.stringify(formattedDateTime));
            exitRfid2.focus();
        }

        // Execute AJAX function if both inputs have exactly 12 characters
        if (exitRfid1.value.length === 12 && exitRfid2.value.length === 12) {
            submitExitForm();
        }
    }
    

    function submitExitForm() {
        const formData = new FormData();
        const rfid1Timestamp = localStorage.getItem('rfid1Timestamp');
        
        formData.append('rfid1', exitRfid1.value);
        formData.append('rfid2', exitRfid2.value);
        formData.append('rfid1Date', rfid1Timestamp ? JSON.parse(rfid1Timestamp).date : '');
        formData.append('rfid1Time', rfid1Timestamp ? JSON.parse(rfid1Timestamp).time : '');

        console.log("Sending data: ", {
            rfid1: exitRfid1.value,
            rfid2: exitRfid2.value,
            rfid1Date: rfid1Timestamp ? JSON.parse(rfid1Timestamp).date : '',
            rfid1Time: rfid1Timestamp ? JSON.parse(rfid1Timestamp).time : ''
        });

        fetch('/exitSmartGate', {
            method: 'POST',
            body: formData
        })
        .then(response => response.text())
        .then(data => {
            if (data === 'success') {
                console.log('Exit successful!');
            } else {
                console.log('Error: Invalid RFIDs');
            }
            resetForm();
        })
        .catch(error => {
            console.error('Error:', error);
            resetForm();
        });
    }
    

// Optional function to reset the form after submission


    function resetForm() {
        // Clear the input fields
        exitRfid1.value = '';
        exitRfid2.value = '';

        // Remove the timestamp from local storage
        localStorage.removeItem('rfid1Timestamp');

        // Refocus on the first RFID input field
        exitRfid1.focus();
    }

    // Add event listeners to inputs
    exitRfid1.addEventListener('input', checkInputs);
    exitRfid2.addEventListener('input', checkInputs);
});
    function formSubmit() {
        const toggle = document.getElementById('toggle');
        const toggleLabel = document.getElementById('toggle-label');
        const toggleState = document.getElementById('toggleState');
        const toggleState2 = document.getElementById('toggleState2');
        const dot = document.querySelector('.dot');

        if (toggle.checked) {
            toggleLabel.textContent = 'System ON';
            toggleState.value = 'ON';
            toggleState2.value = 'ON';
            dot.style.transform = 'translateX(100%)';
            dot.style.backgroundColor = '#4CAF50';
        } else {
            toggleLabel.textContent = 'System OFF';
            toggleState.value = 'OFF';
            toggleState2.value = 'OFF';
            dot.style.transform = 'translateX(0)';
            dot.style.backgroundColor = '#ffffff';
        }

        document.getElementById('toggleForm').submit();
    }
	document.addEventListener('DOMContentLoaded', function() {
    const rfidInput = document.getElementById('rfid');
    const toggleState2 = document.getElementById('toggleState2');
    const gateForm = document.getElementById('gateForm'); // Ensure your form has this ID
    let rfidCode = '';

    // Prevent the form from submitting
    gateForm.addEventListener('submit', function(event) {
        event.preventDefault(); // Prevent default form submission
    });

    // Focus the input field after the page has loaded
    rfidInput.focus();

    rfidInput.addEventListener('input', function() {
        rfidCode += rfidInput.value.trim(); // Append input to rfidCode
        rfidInput.value = ''; // Clear input field for further entries

        if (rfidCode.length === 12) { // Check if the full RFID code (12 characters) is entered
            submitExitForm(rfidCode); // Pass the RFID code directly
        }
    });

    function submitExitForm(rfidCode) {
        // Create a FormData object to hold the form data
        const formData = new FormData();
        formData.append('rfid', rfidCode); // Use the full RFID code
        formData.append('toggleState2', toggleState2.value);

        // Send the form data using fetch
        fetch('/enterSmartGate', {
            method: 'POST',
            body: formData
        })
        .then(response => response.json()) // Parse the response as JSON
        .then(data => {
            if (data.successMessage) {
               console.log("sucessss")
            } else if (data.errorMessage) {
                // Handle error response, e.g., show an error message
                Swal.fire({
					toast: true,
                    icon: 'error',
                    title: 'Oops...',
                    text: data.errorMessage,
                });
            }

            resetForm(); // Reset the form after submission
        })
        .catch(error => {
            console.error('Error:', error);
            Swal.fire({
                icon: 'error',
                title: 'Error occurred',
                text: 'An error occurred during form submission.'
            });
            resetForm();
        });
    }

    // Optional function to reset the form after submission
    function resetForm() {
        rfidCode = ''; // Clear the stored RFID code
        rfidInput.value = ''; // Clear the input field
        rfidInput.focus(); // Refocus on the RFID input field
    }
});


    function openVisitorModal() {
        document.getElementById('visitorEntryModal').classList.remove('hidden');
    }

    document.getElementById('closeModal').addEventListener('click', function() {
        document.getElementById('visitorEntryModal').classList.add('hidden');
    });

   
    window.onload = function() {
        const toggle = document.getElementById('toggle');
        const toggleLabel = document.getElementById('toggle-label');
        const toggleState = document.getElementById('toggleState');
        const toggleState2 = document.getElementById('toggleState2');
        const dot = document.querySelector('.dot');

        // Get the initial state from localStorage or set a default
        const isOn = localStorage.getItem('toggleState') === 'ON';

        // Set initial state
        toggle.checked = isOn;
        toggleLabel.textContent = isOn ? 'System ON' : 'System OFF';
        toggleState.value = isOn ? 'ON' : 'OFF';
        toggleState2.value = isOn ? 'ON' : 'OFF';

        // Set initial dot position and color
        if (isOn) {
            dot.style.transform = 'translateX(100%)';
            dot.style.backgroundColor = '#4CAF50';
        } else {
            dot.style.transform = 'translateX(0)';
            dot.style.backgroundColor = '#ffffff';
        }

        // Add event listener for changes
        toggle.addEventListener('change', function() {
            const newState = toggle.checked ? 'ON' : 'OFF';
            localStorage.setItem('toggleState', newState);
            toggleLabel.textContent = toggle.checked ? 'System ON' : 'System OFF';
            toggleState.value = newState;
            toggleState2.value = newState;

            // Animate dot position and color
            if (toggle.checked) {
                dot.style.transform = 'translateX(100%)';
                dot.style.backgroundColor = '#4CAF50';
            } else {
                dot.style.transform = 'translateX(0)';
                dot.style.backgroundColor = '#ffffff';
            }
        });
    };
	  
	document.addEventListener('DOMContentLoaded', function () {
	    var sidebar = document.getElementById('sidebar');
	    var topBar = document.querySelector('.top-bar');
	    var mainContent = document.getElementById('main-content');
	    var footerTextFull = document.querySelector('.footer-text-full');
	    var footerTextCollapsed = document.querySelector('.footer-text-collapsed');
	    var dropdownMenu = document.getElementById('dropdown-menu');
	    var powerButton = document.getElementById('power-button');

	    // Function to update layout based on sidebar state
	    function updateLayout() {
	        const savedState = localStorage.getItem('sidebarState');
	        if (savedState === 'minimized') {
	            sidebar.classList.add('minimized');
	            sidebar.classList.remove('expanded');
	            footerTextFull.classList.add('hidden');
	            footerTextCollapsed.classList.remove('hidden');
	        } else {
	            sidebar.classList.remove('minimized');
	            sidebar.classList.add('expanded');
	            footerTextFull.classList.remove('hidden');
	            footerTextCollapsed.classList.add('hidden');
	        }

	        // Update other elements based on sidebar state
	        document.querySelectorAll('.sidebar-expanded').forEach(el => el.classList.toggle('hidden', sidebar.classList.contains('minimized')));
	        document.querySelectorAll('.sidebar-collapsed').forEach(el => el.classList.toggle('hidden', !sidebar.classList.contains('minimized')));
	        document.querySelectorAll('.toggle-bar span').forEach(el => el.classList.toggle('hidden', sidebar.classList.contains('minimized')));
	        topBar.classList.toggle('minimized', sidebar.classList.contains('minimized'));
	        mainContent.classList.toggle('minimized', sidebar.classList.contains('minimized'));

	        // Ensure dropdown menu is hidden by default
	        dropdownMenu.classList.remove('show');
	        dropdownMenu.classList.add('hide');
	        dropdownMenu.style.display = 'none';
	    }

	    // Call updateLayout on page load
	    updateLayout();

	    // Save sidebar state
	    function saveSidebarState() {
	        const isMinimized = sidebar.classList.contains('minimized');
	        localStorage.setItem('sidebarState', isMinimized ? 'minimized' : 'expanded');
	    }

	    // Toggle Sidebar
	    document.getElementById('sidebar-toggle').addEventListener('click', function () {
	        sidebar.classList.toggle('minimized');
	        sidebar.classList.toggle('expanded');
	        saveSidebarState();
	        updateLayout();
	    });

	    // Power button dropdown functionality
	    powerButton.addEventListener('click', function (event) {
	        event.stopPropagation();
	        dropdownMenu.classList.toggle('show');
	        dropdownMenu.classList.toggle('hide');
	        dropdownMenu.style.display = dropdownMenu.classList.contains('show') ? 'block' : 'none';
	    });

	    // Optional: Close dropdown if clicked outside
	    document.addEventListener('click', function (event) {
	        if (!powerButton.contains(event.target) && !dropdownMenu.contains(event.target)) {
	            dropdownMenu.classList.add('hide');
	            dropdownMenu.classList.remove('show');
	            dropdownMenu.style.display = 'none';
	        }
	    });

	    // Toggle button functionality
	    const button = document.getElementById('toggle-button');
	    const circle = document.getElementById('toggle-circle');
	    const onText = document.getElementById('on-text');
	    const offText = button.querySelector('span.text-gray-700');

	    // Load saved state from localStorage
	    const isOn = localStorage.getItem('toggleState') === 'on';
	    if (isOn) {
	        button.classList.add('bg-purple-400');
	        circle.classList.add('translate-x-6');
	        onText.classList.remove('hidden');
	        offText.classList.add('hidden');
	    }

	    button.addEventListener('click', () => {
	        const isOn = button.classList.toggle('bg-purple-400');
	        circle.classList.toggle('translate-x-6');
	        onText.classList.toggle('hidden');
	        offText.classList.toggle('hidden');
	        localStorage.setItem('toggleState', isOn ? 'on' : 'off');
	    });
	});
