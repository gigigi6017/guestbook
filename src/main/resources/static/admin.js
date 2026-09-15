// 관리자 로그인 버튼
const loginButton = document.getElementById("loginButton");

// 관리자 비밀번호 입력창
const adminPassword = document.getElementById("adminPassword");

// 로그인 결과 메시지
const loginMessage = document.getElementById("loginMessage");

// 관리자 기능 영역
const adminArea = document.getElementById("adminArea");

// 로그인 영역
const loginArea = document.getElementById("loginArea");


// 로그인 버튼 클릭
loginButton.addEventListener("click", async () => {

    // 입력한 관리자 비밀번호 가져오기
    const password = adminPassword.value.trim();

    // 비밀번호 미입력 확인
    if (password === "") {
        loginMessage.textContent = "비밀번호를 입력해주세요.";
        loginMessage.style.color = "red";
        return;
    }

    // 로그인 중 중복 클릭 방지
    loginButton.disabled = true;
    loginButton.textContent = "로그인 중...";

    try {

        // 관리자 로그인 요청
        const response = await fetch("/api/admin/login", {
            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            // 세션 쿠키 포함
            credentials: "include",

            body: JSON.stringify({
                password: password
            })
        });

        // 서버 응답 내용 받기
        const result = await response.json();

        // 로그인 성공
        if (response.ok) {

            loginMessage.textContent =
                result.message || "관리자 로그인 성공!";

            loginMessage.style.color = "green";

            // 로그인 영역 숨기기
            loginArea.style.display = "none";

            // 관리자 기능 영역 보이기
            adminArea.style.display = "block";

            // 관리자 방명록 목록 불러오기
            loadGuestbooks();

        } else {

            // 로그인 실패 메시지 표시
            loginMessage.textContent =
                result.message || "관리자 비밀번호가 올바르지 않습니다.";

            loginMessage.style.color = "red";

            // 비밀번호 입력창 초기화
            adminPassword.value = "";

        }

    } catch (error) {

        console.error("관리자 로그인 오류:", error);

        loginMessage.textContent =
            "서버와 연결할 수 없습니다.";

        loginMessage.style.color = "red";

    } finally {

        // 버튼 다시 활성화
        loginButton.disabled = false;
        loginButton.textContent = "로그인";
    }

});


// 엔터 키로 로그인 가능
adminPassword.addEventListener("keydown", (event) => {

    if (event.key === "Enter") {
        loginButton.click();
    }

});


// 관리자용 방명록 목록 불러오기
async function loadGuestbooks() {

    try {

        const response = await fetch("/guestbook/get", {
            method: "GET",
            credentials: "include"
        });

        if (!response.ok) {
            throw new Error("방명록 조회 실패");
        }

        const guestbooks = await response.json();

        const list = document.getElementById("guestbookList");

        list.innerHTML = "";

        // 방명록이 없는 경우
        if (guestbooks.length === 0) {

            list.textContent = "등록된 방명록이 없습니다.";
            return;
        }

        // 방명록 표시
        guestbooks.forEach((guestbook) => {

            const div = document.createElement("div");

            div.innerHTML = `
                <hr>

                <p>
                    <strong>이름:</strong>
                    ${guestbook.name}
                </p>

                <p>
                    <strong>평점:</strong>
                    ${guestbook.rating}
                </p>

                <p>
                    <strong>내용:</strong>
                    ${guestbook.content}
                </p>

                <button type="button">
                    관리자 삭제
                </button>
            `;

            // 삭제 버튼
            const deleteButton = div.querySelector("button");

            deleteButton.addEventListener("click", () => {
                deleteGuestbook(guestbook.id);
            });

            list.appendChild(div);

        });

    } catch (error) {

        console.error("방명록 조회 오류:", error);

        document.getElementById("guestbookList").textContent =
            "방명록을 불러오지 못했습니다.";

    }

}


// 관리자 방명록 삭제
async function deleteGuestbook(id) {

    const confirmed = confirm(
        "정말 이 방명록을 삭제하시겠습니까?"
    );

    if (!confirmed) {
        return;
    }

    try {

        const response = await fetch(
            `/guestbook/admin-delete?id=${id}`,
            {
                method: "DELETE",
                credentials: "include"
            }
        );

        const message = await response.text();

        if (response.ok) {

            alert(message);

            // 삭제 후 목록 새로고침
            loadGuestbooks();

        } else {

            alert(message || "삭제에 실패했습니다.");

        }

    } catch (error) {

        console.error("관리자 삭제 오류:", error);

        alert("서버와 연결할 수 없습니다.");

    }

}